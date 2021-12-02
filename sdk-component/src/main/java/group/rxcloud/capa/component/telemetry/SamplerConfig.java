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
import group.rxcloud.capa.component.telemetry.metrics.CapaMeterProviderBuilder;
import group.rxcloud.capa.infrastructure.CapaProperties;
import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Sampler config.
 */
public class SamplerConfig implements Serializable {

    private static final long serialVersionUID = -2113523925814197551L;

    public static final transient String FILE_PATH = "capa-component-telemetry-sample.properties";

    public static final transient String COMMON_FILE_SUFFIX = "telemetry-common";

    /**
     * Sample all data as default.
     */
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig() {{
        setTraceEnable(true);
        setMetricsEnable(true);
    }};

    public static final transient SamplerConfig CONFIG = new SamplerConfig();

    public static final transient Supplier<SamplerConfig> DEFAULT_SUPPLIER = () -> {
        return CONFIG;
    };

    private static final transient Logger log = LoggerFactory.getLogger(CapaMeterProviderBuilder.class);


    static {
        Mixer.configurationHooksNullable().ifPresent(hooks -> {
            try {
                subscribeConfiguration(hooks, hooks.defaultConfigurationAppId(), true);
            } catch (Throwable throwable) {
                log.warn("Fail to load global telemetry config. Dynamic global config is disabled for capa telemetry.",
                        throwable);
            }
            try {
                subscribeConfiguration(hooks,
                        CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply(COMMON_FILE_SUFFIX).getProperty("appId"),
                        false);
            } catch (Throwable throwable) {
                log.warn("Fail to load global telemetry config. Dynamic global config is disabled for capa telemetry.",
                        throwable);
            }
        });

    }

    private Boolean metricsEnable;

    private Boolean traceEnable;

    private static void subscribeConfiguration(ConfigurationHooks configurationHooks, String appId, boolean prior) {
        String storeName = configurationHooks.registryStoreNames().get(0);
        Flux<SubConfigurationResp<SamplerConfig>> configFlux = configurationHooks.subscribeConfiguration(
                storeName,
                appId,
                Lists.newArrayList(FILE_PATH),
                null,
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                TypeRef.get(SamplerConfig.class));
        configFlux.subscribe(resp -> {
            if (CollectionUtils.isNotEmpty(resp.getItems())) {
                SamplerConfig config = resp.getItems().get(0).getContent();
                if (config != null) {
                    if (config.metricsEnable != null && (prior || CONFIG.metricsEnable == null)) {
                        CONFIG.metricsEnable = config.metricsEnable;
                    }
                    if (config.traceEnable != null && (prior || CONFIG.traceEnable == null)) {
                        CONFIG.traceEnable = config.traceEnable;
                    }
                }
            }
        });
    }

    public Boolean isMetricsEnable() {
        return metricsEnable == null ? DEFAULT_CONFIG.metricsEnable : metricsEnable;
    }

    public void setMetricsEnable(boolean metricsEnable) {
        this.metricsEnable = metricsEnable;
    }

    public Boolean isTraceEnable() {
        return traceEnable == null ? DEFAULT_CONFIG.traceEnable : traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
}
