/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.springboot.pubsub;

import group.rxcloud.vrml.core.beans.SpringContextConfigurator;
import group.rxcloud.capa.component.pubsub.CapaPubSub;
import group.rxcloud.capa.component.pubsub.NewMessage;
import group.rxcloud.capa.component.pubsub.SubscribeRequest;
import group.rxcloud.capa.pubsub.domain.TopicEventRequest;
import group.rxcloud.capa.pubsub.domain.TopicSubscription;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Defined customer topic subscriber implement.
 */
public abstract class TopicSubscriber {

    /**
     * The PubSub clients to be used.
     *
     * @see CapaPubSub
     */
    private final Map<String, CapaPubSub> pubSubs;

    public TopicSubscriber(List<CapaPubSub> pubSubs) {
        if (pubSubs == null || pubSubs.isEmpty()) {
            Map<String, CapaPubSub> beans = SpringContextConfigurator.getBeans(CapaPubSub.class);
            if (beans == null || beans.isEmpty()) {
                this.pubSubs = new ConcurrentHashMap<>(2, 1);
            } else {
                this.pubSubs = beans.values().stream()
                        .collect(Collectors.toMap(
                                CapaPubSub::getPubSubName,
                                Function.identity()));
            }
        } else {
            this.pubSubs = pubSubs.stream()
                    .collect(Collectors.toMap(
                            CapaPubSub::getPubSubName,
                            Function.identity()));
        }
    }

    /**
     * Subscribe {@link TopicSubscription} and generate the message Flux.
     *
     * @return the Flux of {@link TopicEventRequest}
     */
    public Flux<TopicEventRequest> doSubscribe(TopicSubscription topicSubscription) {
        final String pubSubName = topicSubscription.getPubSubName();
        CapaPubSub pubSub = this.getPubSub(pubSubName);

        SubscribeRequest subscribeRequest = this.getSubscribeRequest(topicSubscription);

        Flux<NewMessage> newMessageFlux = pubSub.subscribe(subscribeRequest);
        Flux<TopicEventRequest> topicEventRequestFlux = newMessageFlux
                .map(newMessage -> this.getTopicEventRequest(pubSubName, newMessage));
        return topicEventRequestFlux;
    }

    private CapaPubSub getPubSub(String pubsubName) {
        // check pubsubName
        if (pubsubName == null || pubsubName.trim().isEmpty()) {
            throw new IllegalArgumentException("PubSub Name cannot be null or empty.");
        }
        return Objects.requireNonNull(pubSubs.get(pubsubName), "PubSub Component cannot be null.");
    }

    private SubscribeRequest getSubscribeRequest(TopicSubscription topicSubscription) {
        final String topicName = topicSubscription.getTopicName();
        final Map<String, String> metadata = topicSubscription.getMetadata();
        SubscribeRequest subscribeRequest = new SubscribeRequest(topicName);
        subscribeRequest.setMetadata(metadata);
        return subscribeRequest;
    }

    private TopicEventRequest getTopicEventRequest(String pubSubName, NewMessage newMessage) {
        TopicEventRequest topicEventRequest = new TopicEventRequest();
        topicEventRequest.setPubsubName(pubSubName);
        topicEventRequest.setTopic(newMessage.getTopic());
        topicEventRequest.setData(newMessage.getData());
        topicEventRequest.setMetadata(newMessage.getMetadata());
        topicEventRequest.setSpecVersion(CapaPubSub.API_VERSION);
        return topicEventRequest;
    }
}
