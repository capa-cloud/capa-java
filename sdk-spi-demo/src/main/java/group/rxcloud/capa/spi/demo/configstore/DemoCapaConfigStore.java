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
package group.rxcloud.capa.spi.demo.configstore;

import group.rxcloud.capa.component.configstore.ConfigurationItem;
import group.rxcloud.capa.component.configstore.StoreConfig;
import group.rxcloud.capa.component.configstore.SubscribeResp;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.spi.configstore.CapaConfigStoreSpi;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DemoCapaConfigStore extends CapaConfigStoreSpi {

    private static final Logger logger = LoggerFactory.getLogger(DemoCapaConfigStore.class);

    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public DemoCapaConfigStore(CapaObjectSerializer objectSerializer) {
        super(objectSerializer);
    }

    @Override
    protected void doInit(StoreConfig storeConfig) {
        // paas
    }

    @Override
    public String stopSubscribe() {
        // paas
        return null;
    }

    @Override
    protected <T> Mono<List<ConfigurationItem<T>>> doGet(String appId, String group, String label, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        ConfigurationItem<T> configurationItems = new ConfigurationItem<>();
        configurationItems.setKey("test");
        configurationItems.setContent(null);
        configurationItems.setGroup(getDefaultGroup());
        configurationItems.setLabel(getDefaultLabel());
        configurationItems.setTags(metadata);
        configurationItems.setMetadata(metadata);

        List<ConfigurationItem<T>> items = Collections.singletonList(configurationItems);
        return Mono.just(items);
    }

    @Override
    protected <T> Flux<SubscribeResp<T>> doSubscribe(String appId, String group, String label, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        return Flux.interval(Duration.ofSeconds(3))
                .map(aLong -> this.getSubscribeResp(appId, metadata, aLong));
    }

    @NotNull
    private <T> SubscribeResp<T> getSubscribeResp(String appId, Map<String, String> metadata, Long aLong) {
        ConfigurationItem<T> configurationItems = new ConfigurationItem<>();
        configurationItems.setKey("test" + aLong);
        configurationItems.setContent(null);
        configurationItems.setGroup(getDefaultGroup());
        configurationItems.setLabel(getDefaultLabel());
        configurationItems.setTags(metadata);
        configurationItems.setMetadata(metadata);

        List<ConfigurationItem<T>> items = Collections.singletonList(configurationItems);

        SubscribeResp<T> subscribeResp = new SubscribeResp<>();
        subscribeResp.setAppId(appId);
        subscribeResp.setStoreName(getStoreName());
        subscribeResp.setItems(items);
        return subscribeResp;
    }

    @Override
    public void close() throws Exception {
        // paas
    }
}
