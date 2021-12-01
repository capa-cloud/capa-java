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
package group.rxcloud.capa.component.log.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;

/**
 * Log switch configuration
 */
public class LogSwitchConfiguration {

    /**
     * Capa configuration hooks.
     */
    private static final Optional<ConfigurationHooks> configurationHooks;
    /**
     * Log switch config file name.
     */
    private static final String LOGS_SWITCH_CONFIG_FILE_NAME = "log-level-switch.properties";
    /**
     * Log switch config's value.
     */
    private static Map<String, Boolean> logsSwitchConfig = Maps.newHashMap();

    /**
     * Init configuration hooks and subscribe configuration.
     */
    static {
        configurationHooks = Mixer.configurationHooksNullable();
        configurationHooks.ifPresent(configurationHooks -> {
            subscribeConfiguration(configurationHooks);
        });
    }

    /**
     * Subscribe configuration.
     *
     * @param configurationHooks
     */
    private static void subscribeConfiguration(ConfigurationHooks configurationHooks) {
        String storeName = configurationHooks.registryStoreNames().get(0);
        String appId = configurationHooks.defaultConfigurationAppId();
        Flux<SubConfigurationResp<Map>> configFlux = configurationHooks.subscribeConfiguration(
                storeName,
                appId,
                Lists.newArrayList(LOGS_SWITCH_CONFIG_FILE_NAME),
                null,
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                TypeRef.get(Map.class));
        configFlux.subscribe(resp -> {
            if (CollectionUtils.isNotEmpty(resp.getItems())) {
                logsSwitchConfig = resp.getItems().get(0).getContent();
            }
        });
    }

    /**
     * Get the log switch config's values.
     *
     * @return the log switch config's values.
     */
    public static Map<String, Boolean> getLogsSwitchConfig() {
        return logsSwitchConfig;
    }
}
