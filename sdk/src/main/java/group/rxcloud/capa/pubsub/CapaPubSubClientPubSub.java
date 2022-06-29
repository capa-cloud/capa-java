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
package group.rxcloud.capa.pubsub;

import group.rxcloud.capa.component.pubsub.CapaPubSub;
import group.rxcloud.capa.component.pubsub.NewMessage;
import group.rxcloud.capa.component.pubsub.PublishRequest;
import group.rxcloud.capa.component.pubsub.SubscribeRequest;
import group.rxcloud.capa.infrastructure.exceptions.CapaExceptions;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import group.rxcloud.cloudruntimes.domain.core.pubsub.TopicEventRequest;
import group.rxcloud.cloudruntimes.domain.core.pubsub.TopicSubscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An adapter for the PubSub Client.
 */
public class CapaPubSubClientPubSub extends AbstractCapaPubSubClient {

    /**
     * The PubSub clients to be used.
     *
     * @see CapaPubSub
     */
    private final Map<String, CapaPubSub> pubSubs;

    public CapaPubSubClientPubSub(List<CapaPubSub> pubSubs) {
        if (pubSubs == null || pubSubs.isEmpty()) {
            this.pubSubs = new HashMap<>(2, 1);
            this.registryNames = Collections.emptyList();
        } else {
            this.pubSubs = pubSubs.stream()
                    .collect(Collectors.toMap(
                            CapaPubSub::getPubSubName,
                            Function.identity()));
            this.registryNames = new ArrayList<>(this.pubSubs.keySet());
        }
    }

    @Override
    public Mono<String> publishEvent(PublishEventRequest request) {
        try {
            final String pubsubName = request.getPubsubName();
            final CapaPubSub pubSub = this.getPubSub(pubsubName);

            final String topic = request.getTopic();
            // check topic
            if (topic == null || topic.trim().isEmpty()) {
                throw new IllegalArgumentException("[Capa] topic cannot be null or empty.");
            }

            final Object data = request.getData();
            // check data
            if (data == null) {
                throw new IllegalArgumentException("[Capa] data cannot be null or empty.");
            }

            Map<String, String> metadata = request.getMetadata();
            if (metadata == null || metadata.isEmpty()) {
                metadata = new HashMap<>(2, 1);
            }

            final String contentType = request.getContentType();
            final Map<String, String> finalMetadata = metadata;

            return Mono.subscriberContext()
                    .flatMap(context -> {
                        PublishRequest publishRequest = this.getPublishRequest(
                                pubsubName,
                                topic,
                                data,
                                contentType,
                                finalMetadata);
                        return pubSub.publish(publishRequest);
                    })
                    .flatMap(s -> {
                        if (s == null || s.isEmpty()) {
                            return Mono.empty();
                        }
                        return Mono.just(s);
                    });
        } catch (Exception ex) {
            return CapaExceptions.wrapMono(ex);
        }
    }

    @Override
    public Flux<TopicEventRequest> subscribeEvents(TopicSubscription topicSubscription) {
        try {
            final String pubSubName = topicSubscription.getPubSubName();
            final CapaPubSub pubSub = this.getPubSub(pubSubName);

            return Flux.deferWithContext(Mono::just)
                    .flatMap(context -> {
                        SubscribeRequest subscribeRequest = this.getSubscribeRequest(topicSubscription);
                        return pubSub.subscribe(subscribeRequest);
                    })
                    .flatMap(newMessage -> {
                        if (newMessage == null) {
                            return Flux.empty();
                        }
                        TopicEventRequest topicEventRequest = this.getTopicEventRequest(pubSubName, newMessage);
                        return Flux.just(topicEventRequest);
                    });
        } catch (Exception ex) {
            return CapaExceptions.wrapFlux(ex);
        }
    }

    private CapaPubSub getPubSub(String pubsubName) {
        // check pubsubName
        if (pubsubName == null || pubsubName.trim().isEmpty()) {
            throw new IllegalArgumentException("[Capa] PubSub Name cannot be null or empty.");
        }
        return Objects.requireNonNull(pubSubs.get(pubsubName), "[Capa] PubSub Component cannot be null.");
    }

    private PublishRequest getPublishRequest(String pubsubName, String topic, Object data, String contentType, Map<String, String> metadata) {
        PublishRequest publishRequest = new PublishRequest(pubsubName, topic, data);
        publishRequest.setContentType(contentType);
        publishRequest.setMetadata(metadata);
        return publishRequest;
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

    @Override
    public void close() {
        try {
            for (CapaPubSub pubSub : pubSubs.values()) {
                pubSub.close();
            }
        } catch (Exception e) {
            throw CapaExceptions.propagate(e);
        }
    }
}
