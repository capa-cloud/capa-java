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
package group.rxcloud.capa.spi.configstore;

import group.rxcloud.capa.component.configstore.CapaConfigStore;
import group.rxcloud.capa.component.configstore.ConfigurationItem;
import group.rxcloud.capa.component.configstore.GetRequest;
import group.rxcloud.capa.component.configstore.SubscribeReq;
import group.rxcloud.capa.component.configstore.SubscribeResp;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * The SPI Capa ConfigStore client. Templates for different implementations.
 */
public abstract class CapaConfigStoreSpi extends CapaConfigStore {

    private static final Logger logger = LoggerFactory.getLogger(CapaConfigStoreSpi.class);

    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public CapaConfigStoreSpi(CapaObjectSerializer objectSerializer) {
        super(objectSerializer);
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> get(GetRequest getRequest, TypeRef<T> type) {
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaConfigStoreSpi] get config request[{}]", getRequest);
        }
        final String appId = getRequest.getAppId();
        final String group = getRequest.getGroup();
        final String label = getRequest.getLabel();
        final Map<String, String> metadata = getRequest.getMetadata();
        // [value1, value2, ...]
        final List<String> keys = getRequest.getKeys();

        return doGet(appId, group, label, keys, metadata, type);
    }

    /**
     * Get configuration.
     *
     * @param <T>      the response type parameter
     * @param appId    the app id
     * @param group    the group
     * @param label    the label
     * @param keys     the keys
     * @param metadata the metadata
     * @param type     the response type
     * @return the async mono response
     */
    protected abstract <T> Mono<List<ConfigurationItem<T>>> doGet(String appId,
                                                                  String group,
                                                                  String label,
                                                                  List<String> keys,
                                                                  Map<String, String> metadata,
                                                                  TypeRef<T> type);

    @Override
    public <T> Flux<SubscribeResp<T>> subscribe(SubscribeReq subscribeReq, TypeRef<T> type) {
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaConfigStoreSpi] subscribe config request[{}]", subscribeReq);
        }
        final String appId = subscribeReq.getAppId();
        final String group = subscribeReq.getGroup();
        final String label = subscribeReq.getLabel();
        final Map<String, String> metadata = subscribeReq.getMetadata();
        // [value1, value2, ...]
        final List<String> keys = subscribeReq.getKeys();

        return doSubscribe(appId, group, label, keys, metadata, type);
    }

    /**
     * Subscribe configuration.
     *
     * @param <T>      the response type parameter
     * @param appId    the app id
     * @param group    the group
     * @param label    the label
     * @param keys     the keys
     * @param metadata the metadata
     * @param type     the response type
     * @return the async flux of response stream
     */
    protected abstract <T> Flux<SubscribeResp<T>> doSubscribe(String appId,
                                                              String group,
                                                              String label,
                                                              List<String> keys,
                                                              Map<String, String> metadata,
                                                              TypeRef<T> type);

    @Override
    public String getDefaultGroup() {
        return "application";
    }

    @Override
    public String getDefaultLabel() {
        return "";
    }
}
