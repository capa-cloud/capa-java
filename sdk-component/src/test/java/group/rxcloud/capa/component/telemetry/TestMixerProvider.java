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
package group.rxcloud.capa.component.telemetry;

import com.google.common.collect.Lists;
import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import group.rxcloud.capa.infrastructure.hook.TelemetryHooks;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @author: chenyijiang
 * @date: 2021/12/2 12:39
 */
public class TestMixerProvider implements Mixer.MixerProvider {

    SamplerConfig app = new SamplerConfig() {{setMetricsEnable(false);}};
    SamplerConfig global = new SamplerConfig() {{setTraceEnable(false); setMetricsEnable(true);}};


    private ConfigurationHooks configurationHooks = new ConfigurationHooks() {
        @Override
        public List<String> registryStoreNames() {
            return Lists.newArrayList("QConfig");
        }

        @Override
        public String defaultConfigurationAppId() {
            return "1234567";
        }

        @Override
        public <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(String storeName, String appId, List<String> keys, Map<String, String> metadata, String group, String label, TypeRef<T> type) {

            if (type.getType() == SamplerConfig.class) {
                if ("123".equals(appId)) {
                    return Flux.just(getSubscribeResponse(global));
                }
                if (defaultConfigurationAppId().equals(appId)) {
                    return Flux.just(getSubscribeResponse(app));
                }
                return Flux.just(null);
            }
            return ConfigurationHooks.super.subscribeConfiguration(storeName, appId, keys, metadata, type);
        }
    };

    private <T> SubConfigurationResp<T> getSubscribeResponse(SamplerConfig samplerConfig) {
        SubConfigurationResp<T> subConfigurationResp = new SubConfigurationResp<>();
        ConfigurationItem item = new ConfigurationItem();
        item.setContent(samplerConfig);
        subConfigurationResp.setItems(Lists.newArrayList(item));
        return subConfigurationResp;
    }

    @Override
    public ConfigurationHooks provideConfigurationHooks() {
        return configurationHooks;
    }

    @Override
    public TelemetryHooks provideTelemetryHooks() {
        return null;
    }


}
