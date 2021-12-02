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

/**
 * Config provider to merge multiple properties file which takes the input order as their priority.
 */
public class MergedPropertiesConfig {

    private final String fileName;

    private final Properties[] properties;

    private final Object lock = new Object();

    private Map<String, String> merged;

    public MergedPropertiesConfig(String fileName, String... appIds) {
        this.fileName = fileName;
        properties = new Properties[appIds.length];
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

        configFlux.subscribe(resp -> {
            if (!resp.getItems().isEmpty()) {
                properties[index] = resp.getItems().get(0).getContent();
            } else {
                properties[index] = null;
            }
        });

        synchronized (lock) {
            Map<String, String> merged = new HashMap<>();
            for (Properties item : properties) {
                if (item != null) {
                    item.forEach((k, v) -> merged.putIfAbsent(String.valueOf(k), String.valueOf(v)));
                }
            }
            this.merged = merged;
        }
    }
}
