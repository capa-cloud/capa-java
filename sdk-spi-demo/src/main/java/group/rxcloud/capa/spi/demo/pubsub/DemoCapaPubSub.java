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
package group.rxcloud.capa.spi.demo.pubsub;

import group.rxcloud.capa.component.pubsub.CapaPubSub;
import group.rxcloud.capa.component.pubsub.NewMessage;
import group.rxcloud.capa.component.pubsub.PubSubConfig;
import group.rxcloud.capa.component.pubsub.PublishRequest;
import group.rxcloud.capa.component.pubsub.SubscribeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DemoCapaPubSub extends CapaPubSub {

    private static final Logger logger = LoggerFactory.getLogger(DemoCapaPubSub.class);

    @Override
    public void init(PubSubConfig pubSubConfig) {
        logger.info("[DemoCapaPubSub.init] pubSubConfig[{}]", pubSubConfig);
    }

    @Override
    public List<String> features() {
        return Collections.emptyList();
    }

    @Override
    public Mono<String> publish(PublishRequest request) {
        logger.info("[DemoCapaPubSub.publish] request[{}]", request);
        String messageId = UUID.randomUUID().toString();
        return Mono.just(messageId);
    }

    @Override
    public Flux<NewMessage> subscribe(SubscribeRequest request) {
        return Flux.interval(Duration.ofSeconds(3))
                .map(aLong -> {
                    NewMessage newMessage = this.generateNewMessage(request.getTopic(), aLong, request.getMetadata());
                    logger.info("[DemoCapaPubSub.subscribe] generate newMessage[{}]", newMessage);
                    return newMessage;
                });
    }

    private NewMessage generateNewMessage(String topic, Long aLong, Map<String, String> metadata) {
        NewMessage newMessage = new NewMessage(topic, aLong);
        newMessage.setMetadata(metadata);
        return newMessage;
    }
}
