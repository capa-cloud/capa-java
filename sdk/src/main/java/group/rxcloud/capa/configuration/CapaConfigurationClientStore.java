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
import group.rxcloud.capa.component.configstore.GetRequest;
import group.rxcloud.capa.component.configstore.SubscribeReq;
import group.rxcloud.capa.component.configstore.SubscribeResp;
import group.rxcloud.capa.infrastructure.exceptions.CapaExceptions;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
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
 * An adapter for the Config Store Client.
 */
public class CapaConfigurationClientStore extends AbstractCapaConfigurationClient {

    /**
     * The Store clients to be used.
     *
     * @see CapaConfigStore
     */
    private final Map<String, CapaConfigStore> configStores;

    public CapaConfigurationClientStore(List<CapaConfigStore> stores) {
        if (stores == null || stores.isEmpty()) {
            this.configStores = new HashMap<>(2, 1);
            this.registryNames = Collections.emptyList();
        } else {
            this.configStores = stores.stream()
                    .collect(Collectors.toMap(
                            CapaConfigStore::getStoreName,
                            Function.identity()));
            this.registryNames = new ArrayList<>(this.configStores.keySet());
        }
    }

    private CapaConfigStore getStore(String storeName) {
        // check storeName
        if (storeName == null || storeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Store Name cannot be null or empty.");
        }
        return Objects.requireNonNull(configStores.get(storeName), "Store Component cannot be null.");
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        return getConfiguration(storeName, appId, keys, metadata, null, null, type);
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, TypeRef<T> type) {
        return getConfiguration(storeName, appId, keys, metadata, group, null, type);
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, String label, TypeRef<T> type) {
        try {
            final CapaConfigStore store = this.getStore(storeName);

            if (group == null || group.trim().isEmpty()) {
                group = store.getDefaultGroup();
            }
            if (label == null || label.trim().isEmpty()) {
                label = store.getDefaultLabel();
            }
            if (keys == null || keys.isEmpty()) {
                keys = new ArrayList<>(2);
            }
            if (metadata == null || metadata.isEmpty()) {
                metadata = new HashMap<>(2, 1);
            }

            final String finalGroup = group;
            final String finalLabel = label;
            final List<String> finalKeys = keys;
            final Map<String, String> finalMetadata = metadata;

            return Mono.subscriberContext()
                    .flatMap(context -> {
                        GetRequest getRequest = this.getStoreRequest(
                                appId,
                                finalGroup,
                                finalLabel,
                                finalKeys,
                                finalMetadata);
                        return store.get(getRequest, type);
                    })
                    .flatMap(configurationItems -> {
                        if (configurationItems == null || configurationItems.isEmpty()) {
                            return Mono.empty();
                        }
                        return Flux.fromStream(configurationItems
                                        .stream()
                                        .map(this::getStoreResponse))
                                .collectList();
                    });
        } catch (Exception ex) {
            return CapaExceptions.wrapMono(ex);
        }
    }

    @Override
    public <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        return subscribeConfiguration(storeName, appId, keys, metadata, null, null, type);
    }

    @Override
    public <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, TypeRef<T> type) {
        return subscribeConfiguration(storeName, appId, keys, metadata, group, null, type);
    }

    @Override
    public <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, String label, TypeRef<T> type) {
        try {
            final CapaConfigStore store = this.getStore(storeName);

            if (group == null || group.trim().isEmpty()) {
                group = store.getDefaultGroup();
            }
            if (label == null || label.trim().isEmpty()) {
                label = store.getDefaultLabel();
            }
            if (keys == null) {
                keys = new ArrayList<>(2);
            }
            if (metadata == null) {
                metadata = new HashMap<>(2, 1);
            }

            final String finalGroup = group;
            final String finalLabel = label;
            final List<String> finalKeys = keys;
            final Map<String, String> finalMetadata = metadata;

            return Flux.deferWithContext(Mono::just)
                    .flatMap(context -> {
                        SubscribeReq subscribeReq = this.getSubscribeRequest(
                                appId,
                                finalGroup,
                                finalLabel,
                                finalKeys,
                                finalMetadata);
                        return store.subscribe(subscribeReq, type);
                    })
                    .flatMap(subscribeResp -> {
                        if (subscribeResp == null) {
                            return Flux.empty();
                        }
                        SubConfigurationResp<T> subscribeResponse = this.getSubscribeResponse(subscribeResp);
                        return Flux.just(subscribeResponse);
                    });
        } catch (Exception ex) {
            return CapaExceptions.wrapFlux(ex);
        }
    }

    private GetRequest getStoreRequest(String appId, String group, String label, List<String> keys, Map<String, String> metadata) {
        GetRequest getRequest = new GetRequest();
        getRequest.setAppId(appId);
        getRequest.setGroup(group);
        getRequest.setLabel(label);
        getRequest.setKeys(keys);
        getRequest.setMetadata(metadata);
        return getRequest;
    }

    private <T> ConfigurationItem<T> getStoreResponse(group.rxcloud.capa.component.configstore.ConfigurationItem<T> tConfigurationItem) {
        ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
        configurationItem.setKey(tConfigurationItem.getKey());
        configurationItem.setContent(tConfigurationItem.getContent());
        configurationItem.setGroup(tConfigurationItem.getGroup());
        configurationItem.setLabel(tConfigurationItem.getLabel());
        configurationItem.setTags(tConfigurationItem.getTags());
        configurationItem.setMetadata(tConfigurationItem.getMetadata());
        return configurationItem;
    }

    private SubscribeReq getSubscribeRequest(String appId, String group, String label, List<String> keys, Map<String, String> metadata) {
        SubscribeReq subscribeReq = new SubscribeReq();
        subscribeReq.setAppId(appId);
        subscribeReq.setGroup(group);
        subscribeReq.setLabel(label);
        subscribeReq.setKeys(keys);
        subscribeReq.setMetadata(metadata);
        return subscribeReq;
    }

    private <T> SubConfigurationResp<T> getSubscribeResponse(SubscribeResp<T> subscribeResp) {
        SubConfigurationResp<T> subConfigurationResp = new SubConfigurationResp<>();
        subConfigurationResp.setStoreName(subscribeResp.getStoreName());
        subConfigurationResp.setAppId(subscribeResp.getAppId());
        if (subscribeResp.getItems() != null && !subscribeResp.getItems().isEmpty()) {
            List<ConfigurationItem<T>> itemList = subscribeResp.getItems().stream()
                    .map(this::getStoreResponse)
                    .collect(Collectors.toList());
            subConfigurationResp.setItems(itemList);
        }
        return subConfigurationResp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            for (CapaConfigStore store : configStores.values()) {
                store.close();
            }
        } catch (Exception e) {
            throw CapaExceptions.propagate(e);
        }
    }
}
