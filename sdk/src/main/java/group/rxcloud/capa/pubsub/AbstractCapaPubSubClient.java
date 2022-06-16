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

import group.rxcloud.capa.AbstractCapaClient;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import group.rxcloud.cloudruntimes.domain.core.pubsub.TopicEventRequest;
import group.rxcloud.cloudruntimes.domain.core.pubsub.TopicSubscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Abstract class with convenient methods common between client implementations.
 *
 * @see CapaPubSubClientPubSub
 */
public abstract class AbstractCapaPubSubClient
        extends AbstractCapaClient
        implements CapaPubSubClient {

    @Override
    public Mono<String> publishEvent(String pubsubName, String topicName, Object data) {
        PublishEventRequest publishEventRequest = new PublishEventRequest(pubsubName, topicName, data);
        return this.publishEvent(publishEventRequest);
    }

    @Override
    public Mono<String> publishEvent(String pubsubName, String topicName, Object data, Map<String, String> metadata) {
        PublishEventRequest request = new PublishEventRequest(pubsubName, topicName, data);
        request.setMetadata(metadata);
        return this.publishEvent(request);
    }

    @Override
    public Flux<TopicEventRequest> subscribeEvents(String pubsubName, String topicName, Map<String, String> metadata) {
        TopicSubscription topicSubscription = new TopicSubscription();
        topicSubscription.setPubSubName(pubsubName);
        topicSubscription.setTopicName(topicName);
        topicSubscription.setMetadata(metadata);
        return this.subscribeEvents(topicSubscription);
    }
}
