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
package group.rxcloud.capa.component.configstore;


import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The Abstract ConfigStore Client. Extend this and provide your specific impl.
 */
public abstract class CapaConfigStore implements AutoCloseable {

    /**
     * Capa API used in this client.
     */
    public static final String API_VERSION = "v1.0";

    /**
     * A utility class for serialize and deserialize the transient objects.
     */
    protected final CapaObjectSerializer objectSerializer;

    /**
     * Init init the configuration store.
     */
    private String storeName;

    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public CapaConfigStore(CapaObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    /**
     * Init init the configuration store.
     */
    public void init(StoreConfig storeConfig) {
        this.storeName = storeConfig.getStoreName();
        this.doInit(storeConfig);
    }

    /**
     * Init init the configuration store.
     */
    protected abstract void doInit(StoreConfig storeConfig);

    /**
     * Gets store name.
     */
    public String getStoreName() {
        return this.storeName;
    }

    /**
     * GetSpecificKeysValue get specific key value.
     */
    public abstract <T> Mono<List<ConfigurationItem<T>>> get(GetRequest getRequest, TypeRef<T> type);

    /**
     * Subscribe subscribe the configurations updates.
     */
    public abstract <T> Flux<SubscribeResp<T>> subscribe(SubscribeReq subscribeReq, TypeRef<T> type);

    /**
     * StopSubscribe stop subs
     */
    public abstract String stopSubscribe();

    /**
     * GetDefaultGroup returns default group.This method will be invoked if a request doesn't specify the group field
     */
    public abstract String getDefaultGroup();

    /**
     * GetDefaultLabel returns default label
     */
    public abstract String getDefaultLabel();
}
