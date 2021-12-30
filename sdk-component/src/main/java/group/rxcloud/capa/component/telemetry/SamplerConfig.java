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

import group.rxcloud.capa.component.CapaTelemetryProperties;
import group.rxcloud.capa.component.telemetry.metrics.CapaMeterProviderBuilder;
import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.MergedPropertiesConfig;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Sampler config.
 */
public class SamplerConfig implements Serializable {

    /**
     * Sample all data as default.
     */
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig() {{
        setTraceEnable(true);
        setMetricsEnable(true);
    }};

    public static final transient AtomicReference<SamplerConfig> REMOTE_CONFIG = new AtomicReference<>();

    private static final int MAX_REMOTE_INIT_RETRY = 5;

    private static final transient AtomicInteger CONFIG_INIT = new AtomicInteger(0);

    private static final long serialVersionUID = -2113523925814197551L;

    private static final transient Logger log = LoggerFactory.getLogger(CapaMeterProviderBuilder.class);

    public static final transient Supplier<SamplerConfig> DEFAULT_SUPPLIER = () -> {
        if (CONFIG_INIT.get() < MAX_REMOTE_INIT_RETRY) {
            synchronized (CONFIG_INIT) {
                if (CONFIG_INIT.getAndIncrement() <= MAX_REMOTE_INIT_RETRY) {
                    if (tryInitRemoteConfig()) {
                        CONFIG_INIT.set(MAX_REMOTE_INIT_RETRY);
                    }
                }
            }
        }

        SamplerConfig config = REMOTE_CONFIG.get();
        if (config == null) {
            config = DEFAULT_CONFIG;
        }
        return config;
    };

    private Boolean metricsEnable;

    private Boolean traceEnable;

    private static boolean tryInitRemoteConfig() {
        Optional<ConfigurationHooks> hooksOpt = Mixer.configurationHooksNullable();
        if (hooksOpt.isPresent()) {
            ConfigurationHooks hooks = hooksOpt.get();
            String fileName = "capa-component-telemetry-sampling.properties";
            try {
                // TODO: 2021/12/3 Move this to SPI module.
                // TODO: 2021/12/3 And use Configuration extension api to get merged file.
                MergedPropertiesConfig config = new MergedPropertiesConfig(
                        fileName,
                        hooks.defaultConfigurationAppId(),
                        CapaTelemetryProperties.Settings.getCenterConfigAppId());
                String metricKey = "metricsEnable";
                String traceKey = "traceEnable";
                SamplerConfig dynamicConfig = new SamplerConfig() {
                    @Override
                    public Boolean isMetricsEnable() {
                        return !config.containsKey(metricKey) || Boolean.TRUE.toString()
                                                                             .equalsIgnoreCase(config.get(metricKey));
                    }

                    @Override
                    public Boolean isTraceEnable() {
                        return !config.containsKey(traceKey) || Boolean.TRUE.toString()
                                                                            .equalsIgnoreCase(config.get(traceKey));
                    }
                };

                REMOTE_CONFIG.set(dynamicConfig);

                return true;
            } catch (Throwable throwable) {
                log.info("Fail to load global telemetry config. Dynamic global config is disabled for capa telemetry.",
                        throwable);
            }
        }
        return false;
    }

    public Boolean isMetricsEnable() {
        return metricsEnable == null ? DEFAULT_SUPPLIER.get().metricsEnable : metricsEnable;
    }

    public void setMetricsEnable(boolean metricsEnable) {
        this.metricsEnable = metricsEnable;
    }

    public Boolean isTraceEnable() {
        return traceEnable == null ? DEFAULT_SUPPLIER.get().traceEnable : traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
}
