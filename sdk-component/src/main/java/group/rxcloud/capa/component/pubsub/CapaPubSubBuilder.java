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

import group.rxcloud.capa.infrastructure.loader.CapaClassLoader;

/**
 * A builder for the {@link CapaPubSub} implementor.
 */
public class CapaPubSubBuilder {

    private final PubSubConfig pubSubConfig;

    /**
     * Creates a constructor for CapaPubSub.
     *
     * @param pubSubConfig the pubsub config
     */
    public CapaPubSubBuilder(PubSubConfig pubSubConfig) {
        this.pubSubConfig = pubSubConfig;
    }

    /**
     * Build an instance of the client based on the provided setup.
     *
     * @return an instance of {@link CapaPubSub}
     * @throws IllegalStateException if any required field is missing
     */
    public CapaPubSub build() {
        CapaPubSub capaCapaPubSubWorkClient = buildCapaPubSub();
        capaCapaPubSubWorkClient.init(this.pubSubConfig);
        return capaCapaPubSubWorkClient;
    }

    /**
     * Creates an instance of the {@link CapaPubSub} implementor.
     *
     * @return Instance of {@link CapaPubSub} implementor
     */
    private CapaPubSub buildCapaPubSub() {
        // TODO: 2021/11/30 build multi component
        return CapaClassLoader.loadComponentClassObj("pubsub", CapaPubSub.class);
    }
}
