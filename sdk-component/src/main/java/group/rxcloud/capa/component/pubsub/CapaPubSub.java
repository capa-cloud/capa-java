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
package group.rxcloud.capa.component.pubsub;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The Abstract PubSub Client. Extend this and provide your specific impl.
 */
public abstract class CapaPubSub implements AutoCloseable {

    /**
     * Capa API used in this client.
     */
    public static final String API_VERSION = "v1.0";

    /**
     * The pubsub name.
     */
    private String pubsubName;

    /**
     * Gets pubsub name.
     */
    public String getPubSubName() {
        return this.pubsubName;
    }

    /**
     * Init the pubsub.
     *
     * @param pubSubConfig the pubsub config
     */
    public abstract void init(PubSubConfig pubSubConfig);

    /**
     * Features list.
     *
     * @return the feature list
     */
    public abstract List<String> features();

    /**
     * Publish message to pubsub.
     *
     * @param request the request
     * @return the mono plan of the pubsub return
     */
    public abstract Mono<String> publish(PublishRequest request);

    /**
     * Subscribe new messages flux.
     *
     * @param request the request
     * @return the flux of new message.
     */
    public abstract Flux<NewMessage> subscribe(SubscribeRequest request);

    @Override
    public void close() throws Exception {
        // No code needed
    }
}