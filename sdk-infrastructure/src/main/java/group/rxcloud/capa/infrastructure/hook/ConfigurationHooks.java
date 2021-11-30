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
package group.rxcloud.capa.infrastructure.hook;

import group.rxcloud.cloudruntimes.domain.core.ConfigurationRuntimes;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SaveConfigurationRequest;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * The Mixer configuration hooks.
 */
public interface ConfigurationHooks extends ConfigurationRuntimes {

    /**
     * Registry Store Names.
     */
    List<String> registryStoreNames();

    /**
     * Default configuration appI.
     */
    String defaultConfigurationAppId();

    @Override
    default <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, String label, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Mono<List<ConfigurationItem<T>>> getConfiguration(ConfigurationRequestItem configurationRequestItem, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Void> saveConfiguration(SaveConfigurationRequest saveConfigurationRequest) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Void> deleteConfiguration(ConfigurationRequestItem configurationRequestItem) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, String label, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(ConfigurationRequestItem configurationRequestItem, TypeRef<T> type) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }
}
