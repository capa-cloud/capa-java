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


import group.rxcloud.capa.AbstractCapaClient;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SaveConfigurationRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract class with convenient methods common between client implementations.
 *
 * @see CapaConfigurationClientStore
 */
public abstract class AbstractCapaConfigurationClient
        extends AbstractCapaClient
        implements CapaConfigurationClient {

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        return getConfiguration(storeName, appId, keys, metadata, null, null, type);
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, TypeRef<T> type) {
        return getConfiguration(storeName, appId, keys, metadata, group, null, type);
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(ConfigurationRequestItem configurationRequestItem, TypeRef<T> type) {
        Objects.requireNonNull(configurationRequestItem, "[Capa] configurationRequestItem cannot be null.");
        return getConfiguration(configurationRequestItem.getStoreName(),
                configurationRequestItem.getAppId(),
                configurationRequestItem.getKeys(),
                configurationRequestItem.getMetadata(),
                configurationRequestItem.getGroup(),
                configurationRequestItem.getLabel(),
                type);
    }

    @Override
    public Mono<Void> saveConfiguration(SaveConfigurationRequest saveConfigurationRequest) {
        return Mono.error(new UnsupportedOperationException("[Capa] Unsupported save configuration"));
    }

    @Override
    public Mono<Void> deleteConfiguration(ConfigurationRequestItem configurationRequestItem) {
        return Mono.error(new UnsupportedOperationException("[Capa] Unsupported delete configuration"));
    }
}
