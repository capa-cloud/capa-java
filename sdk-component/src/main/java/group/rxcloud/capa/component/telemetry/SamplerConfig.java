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

import group.rxcloud.capa.component.telemetry.metrics.CapaMeterProviderBuilder;
import group.rxcloud.capa.infrastructure.CapaProperties;
import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Sampler config.
 */
public class SamplerConfig implements Serializable {

    public static final transient String FILE_PATH = "capa-sample.properties";

    /**
     * Sample all data as default.
     */
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig();

    private static final transient Logger log = LoggerFactory.getLogger(CapaMeterProviderBuilder.class);

    public static final transient Supplier<SamplerConfig> DEFAULT_SUPPLIER = () -> {
        try {
            String storeName = Optional.ofNullable(CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply("configuration")
                                                                                               .getProperty(
                                                                                                       "CONFIGURATION_COMPONENT_STORE_NAME"))
                                       .orElse("UN_CONFIGURED_STORE_CONFIG_NAME");
            Optional<ConfigurationHooks> hooksOptional = Mixer.configurationHooksNullable();
            if (hooksOptional.isPresent()) {
                List<ConfigurationItem<SamplerConfig>> config = hooksOptional.get().getConfiguration(storeName,
                        null,
                        Collections.singletonList(FILE_PATH),
                        null,
                        "",
                        "",
                        TypeRef.get(SamplerConfig.class)).block();
                if (!config.isEmpty()) {
                    SamplerConfig item = config.get(0).getContent();
                    return item == null ? DEFAULT_CONFIG : item;
                }
            }
        } catch (Throwable throwable) {
            log.warn("Fail to load config item. Dynamic config is disabled for capa telemetry.", throwable);
        }

        return DEFAULT_CONFIG;
    };

    private static final long serialVersionUID = -2113523925814197551L;

    private boolean metricsEnable = true;

    private boolean traceEnable = true;

    public boolean isMetricsEnable() {
        return metricsEnable;
    }

    public void setMetricsEnable(boolean metricsEnable) {
        this.metricsEnable = metricsEnable;
    }

    public boolean isTraceEnable() {
        return traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
}
