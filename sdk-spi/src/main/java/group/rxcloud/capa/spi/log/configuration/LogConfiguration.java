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
package group.rxcloud.capa.spi.log.configuration;

import group.rxcloud.capa.infrastructure.CapaProperties;
import group.rxcloud.capa.infrastructure.hook.MergedPropertiesConfig;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Log switch configuration
 */
public class LogConfiguration {
    /**
     * Log switch config file name.
     */
    private static final String LOG_CONFIGURATION_COMMON_FILE_NAME = "log-configuration.properties";
    private static MergedPropertiesConfig mergedPropertiesConfig;

    static {
        Mixer.configurationHooksNullable().ifPresent(hooks -> {
            String suffix = "log-common";
            try {
                mergedPropertiesConfig = new MergedPropertiesConfig(LOG_CONFIGURATION_COMMON_FILE_NAME,
                        hooks.defaultConfigurationAppId(),
                        CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply(suffix).getProperty("appId"));
            } catch (Throwable throwable) {
                Mixer.telemetryHooksNullable().ifPresent(telemetryHooks -> {
                    Meter meter = telemetryHooks.buildMeter("CloudWatchLogs").block();
                    LongCounter longCounter = meter.counterBuilder("LogsError").build();
                    Optional<LongCounter> longCounterOptional = Optional.ofNullable(longCounter);
                    longCounterOptional.ifPresent(counter -> {
                        longCounter.bind(Attributes.of(AttributeKey.stringKey("LogsConfigurationError"), throwable.getMessage()))
                                .add(1);
                    });
                });
            }
        });
    }

    public static boolean containsKey(String key) {
        try {
            return mergedPropertiesConfig != null && mergedPropertiesConfig.containsKey(key);
        } finally {
            return false;
        }
    }

    public static String get(String key) {
        try {
            return mergedPropertiesConfig == null
                    ? StringUtils.EMPTY
                    : mergedPropertiesConfig.get(key);
        } finally {
            return StringUtils.EMPTY;
        }
    }
}

