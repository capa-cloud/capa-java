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
import group.rxcloud.capa.infrastructure.hook.MergedPropertiesConfig;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
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

    public static final transient SamplerConfig CONFIG = new SamplerConfig();

    private static final long serialVersionUID = -2113523925814197551L;

    private static final transient Logger log = LoggerFactory.getLogger(CapaMeterProviderBuilder.class);

    public static transient Supplier<SamplerConfig> DEFAULT_SUPPLIER = () -> CONFIG;

    static {
        Mixer.configurationHooksNullable().ifPresent(hooks -> {
            String fileName = "capa-component-telemetry-sample.properties";
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

                DEFAULT_SUPPLIER = () -> dynamicConfig;
            } catch (Throwable throwable) {
                log.warn("Fail to load global telemetry config. Dynamic global config is disabled for capa telemetry.",
                        throwable);
            }
        });
    }

    private Boolean metricsEnable;

    private Boolean traceEnable;

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
