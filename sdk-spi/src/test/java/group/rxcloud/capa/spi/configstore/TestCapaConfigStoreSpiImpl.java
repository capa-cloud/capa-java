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

import group.rxcloud.capa.component.configstore.ConfigurationItem;
import group.rxcloud.capa.component.configstore.StoreConfig;
import group.rxcloud.capa.component.configstore.SubscribeResp;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class TestCapaConfigStoreSpiImpl extends CapaConfigStoreSpi {


    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public TestCapaConfigStoreSpiImpl(CapaObjectSerializer objectSerializer) {
        super(objectSerializer);
    }

    @Override
    protected <T> Mono<List<ConfigurationItem<T>>> doGet(String appId, String group, String label, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        List<ConfigurationItem<T>> result = new ArrayList<>();
        keys.forEach(key -> {
            ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
            configurationItem.setKey(key);
            configurationItem.setContent(null);
            configurationItem.setGroup(group);
            configurationItem.setLabel(label);
            configurationItem.setTags(metadata);
            configurationItem.setMetadata(metadata);
            result.add(configurationItem);
        });
        return Mono.just(result);
    }

    @Override
    protected <T> Flux<SubscribeResp<T>> doSubscribe(String appId, String group, String label, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        return Flux.interval(Duration.ofSeconds(3)).map(i -> {
            SubscribeResp<T> subscribeResp = new SubscribeResp<>();
            subscribeResp.setAppId(appId);
            subscribeResp.setStoreName(getStoreName());
            List<ConfigurationItem<T>> list = new ArrayList<>();
            keys.forEach(key -> {
                ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
                configurationItem.setKey(key);
                configurationItem.setContent(null);
                configurationItem.setGroup(group);
                configurationItem.setLabel(label);
                configurationItem.setTags(metadata);
                configurationItem.setMetadata(metadata);
                list.add(configurationItem);
            });
            subscribeResp.setItems(list);
            return subscribeResp;
        });
    }

    @Override
    protected void doInit(StoreConfig storeConfig) {
        //do nothing
    }

    @Override
    public String stopSubscribe() {
        return null;
    }

    @Override
    public void close() throws Exception {
        //do nothing
    }
}
