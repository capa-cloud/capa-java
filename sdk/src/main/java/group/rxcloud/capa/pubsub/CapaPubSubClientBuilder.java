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
import group.rxcloud.capa.component.pubsub.CapaPubSubBuilder;
import group.rxcloud.capa.component.pubsub.PubSubConfig;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A builder for the {@link CapaPubSubClient}.
 */
public class CapaPubSubClientBuilder {

    /**
     * Builder for Capa's PubSub Client.
     */
    private final List<CapaPubSubBuilder> configPubSubBuilders;

    /**
     * Creates a constructor for {@link CapaPubSubClient}.
     */
    public CapaPubSubClientBuilder(PubSubConfig pubSubConfig) {
        this(Collections.singletonList(new CapaPubSubBuilder(pubSubConfig)));
    }

    /**
     * Creates a constructor for {@link CapaPubSubClient} with custom {@link CapaPubSubBuilder}.
     */
    public CapaPubSubClientBuilder(Supplier<CapaPubSubBuilder> capaPubSubBuilderSupplier) {
        this(Collections.singletonList(capaPubSubBuilderSupplier.get()));
    }

    /**
     * Creates a constructor for {@link CapaPubSubClient}.
     */
    public CapaPubSubClientBuilder(List<CapaPubSubBuilder> configPubSubBuilders) {
        this.configPubSubBuilders = configPubSubBuilders;
    }

    /**
     * Build an instance of the Client based on the provided setup.
     *
     * @return an instance of the setup Client
     */
    public CapaPubSubClient build() {
        return buildCapaClientPubSub();
    }

    /**
     * Creates and instance of {@link CapaPubSubClient}.
     */
    private CapaPubSubClient buildCapaClientPubSub() {
        List<CapaPubSub> capaPubSubs = this.configPubSubBuilders.stream()
                .map(CapaPubSubBuilder::build)
                .collect(Collectors.toList());
        return new CapaPubSubClientPubSub(capaPubSubs);
    }
}
