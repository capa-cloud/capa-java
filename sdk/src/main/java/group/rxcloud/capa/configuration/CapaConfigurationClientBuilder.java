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
package group.rxcloud.capa.configuration;


import group.rxcloud.capa.component.configstore.CapaConfigStore;
import group.rxcloud.capa.component.configstore.CapaConfigStoreBuilder;
import group.rxcloud.capa.component.configstore.StoreConfig;
import group.rxcloud.capa.infrastructure.hook.TelemetryHooks;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A builder for the {@link CapaConfigurationClient}.
 */
public class CapaConfigurationClientBuilder {

    /**
     * Builder for Capa's ConfigStore Client.
     */
    private final List<CapaConfigStoreBuilder> configStoreBuilders;

    /**
     * Creates a constructor for {@link CapaConfigurationClient}.
     */
    public CapaConfigurationClientBuilder(StoreConfig storeConfig) {
        this(Collections.singletonList(new CapaConfigStoreBuilder(storeConfig)));
    }

    /**
     * Creates a constructor for {@link CapaConfigurationClient}.
     */
    public CapaConfigurationClientBuilder(StoreConfig storeConfig, TelemetryHooks telemetryHooks) {
        CapaConfigStoreBuilder capaConfigStoreBuilder = new CapaConfigStoreBuilder(storeConfig);
        capaConfigStoreBuilder.withTelemetryHooks(telemetryHooks);
        this.configStoreBuilders = Collections.singletonList(capaConfigStoreBuilder);
    }

    /**
     * Creates a constructor for {@link CapaConfigurationClient} with custom {@link CapaConfigStoreBuilder}.
     */
    public CapaConfigurationClientBuilder(Supplier<CapaConfigStoreBuilder> capaConfigStoreBuilderSupplier) {
        this(Collections.singletonList(capaConfigStoreBuilderSupplier.get()));
    }

    /**
     * Creates a constructor for {@link CapaConfigurationClient}.
     */
    public CapaConfigurationClientBuilder(List<CapaConfigStoreBuilder> configStoreBuilders) {
        this.configStoreBuilders = configStoreBuilders;
    }

    /**
     * Build an instance of the Client based on the provided setup.
     *
     * @return an instance of the setup Client
     */
    public CapaConfigurationClient build() {
        return buildCapaClientStore();
    }

    /**
     * Creates and instance of {@link CapaConfigurationClient}.
     */
    private CapaConfigurationClient buildCapaClientStore() {
        List<CapaConfigStore> capaConfigStores = this.configStoreBuilders.stream()
                .map(CapaConfigStoreBuilder::build)
                .collect(Collectors.toList());
        return new CapaConfigurationClientStore(capaConfigStores);
    }
}
