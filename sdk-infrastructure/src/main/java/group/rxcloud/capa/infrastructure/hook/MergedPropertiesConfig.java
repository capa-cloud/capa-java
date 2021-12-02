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

import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Config provider to merge multiple properties file which takes the input order as their priority.
 * <p>
 *
 * TODO: 2021/12/3 This should not in infrastructure layer.
 * TODO: 2021/12/3 Use Configuration extension api to get merged file.
 */
@Deprecated
public class MergedPropertiesConfig {

    private final String fileName;

    private final AtomicReferenceArray<Properties> properties;

    private final Object lock = new Object();

    private volatile Map<String, String> merged;

    public MergedPropertiesConfig(String fileName, String... appIds) {
        this.fileName = fileName;
        properties = new AtomicReferenceArray<>(appIds.length);
        merged = new HashMap<>();
        Mixer.configurationHooksNullable().ifPresent(hooks -> {
            for (int i = 0; i < appIds.length; i++) {
                subscribeConfigurationByAppId(hooks, appIds[i], i);
            }
        });
    }

    public boolean containsKey(String key) {
        return merged.containsKey(key);
    }

    public String get(String key) {
        return merged.get(key);
    }

    public Map<String, String> getMerged() {
        return merged;
    }

    private void subscribeConfigurationByAppId(ConfigurationHooks configurationHooks, String appId, int index) {
        String storeName = configurationHooks.registryStoreNames().get(0);

        Flux<SubConfigurationResp<Properties>> configFlux = configurationHooks.subscribeConfiguration(
                storeName,
                appId,
                Collections.singletonList(fileName),
                null,
                "",
                "",
                TypeRef.get(Properties.class));

        // FIXME: 2021/12/3 random callback?
        configFlux.subscribe(resp -> {
            synchronized (lock) {
                if (!resp.getItems().isEmpty()) {
                    properties.set(index, resp.getItems().get(0).getContent());
                } else {
                    properties.set(index, null);
                }

                Map<String, String> merged = new HashMap<>();
                for (int i = 0; i < properties.length(); i++) {
                    Properties item = properties.get(i);
                    if (item != null) {
                        item.forEach((k, v) -> merged.putIfAbsent(String.valueOf(k), String.valueOf(v)));
                    }
                }
                this.merged = merged;
            }
        });
    }
}
