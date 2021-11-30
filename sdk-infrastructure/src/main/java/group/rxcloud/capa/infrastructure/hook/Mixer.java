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

import group.rxcloud.capa.infrastructure.CapaClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The Inner Runtimes Mixer.
 */
public abstract class Mixer {

    private static final Logger logger = LoggerFactory.getLogger(Mixer.class);

    private static ConfigurationHooks configurationHooks;
    private static TelemetryHooks telemetryHooks;

    static {
        try {
            MixerProvider mixerProvider = CapaClassLoader.loadInfrastructureClassObj("mixer", MixerProvider.class);
            if (mixerProvider != null) {
                registerConfigurationHooks(mixerProvider.provideConfigurationHooks());
                registerTelemetryHooks(mixerProvider.provideTelemetryHooks());
            }
        } catch (Exception e) {
            logger.info("[CapaMixer] load empty mixer. expected error: ", e);
        }
    }

    /**
     * The SPI Mixer provider.
     */
    interface MixerProvider {

        /**
         * Provide global configuration hooks.
         *
         * @return the configuration hooks
         */
        ConfigurationHooks provideConfigurationHooks();

        /**
         * Provide global telemetry hooks.
         *
         * @return the telemetry hooks
         */
        TelemetryHooks provideTelemetryHooks();
    }

    /**
     * Register configuration hooks.
     *
     * @param configurationHooks the configuration hooks
     */
    private static void registerConfigurationHooks(ConfigurationHooks configurationHooks) {
        Mixer.configurationHooks = configurationHooks;
    }

    /**
     * Register telemetry hooks.
     *
     * @param telemetryHooks the telemetry hooks
     */
    private static void registerTelemetryHooks(TelemetryHooks telemetryHooks) {
        Mixer.telemetryHooks = telemetryHooks;
    }

    /**
     * Gets configuration hooks.
     *
     * @return the configuration hooks
     */
    @Nullable
    public static ConfigurationHooks configurationHooks() {
        return configurationHooks;
    }

    /**
     * Gets configuration hooks.
     *
     * @return the configuration hooks
     */
    public static Optional<ConfigurationHooks> configurationHooksNullable() {
        return Optional.ofNullable(configurationHooks);
    }

    /**
     * Gets telemetry hooks.
     *
     * @return the telemetry hooks
     */
    @Nullable
    public static TelemetryHooks telemetryHooks() {
        return telemetryHooks;
    }

    /**
     * Gets telemetry hooks.
     *
     * @return the telemetry hooks
     */
    public static Optional<TelemetryHooks> telemetryHooksNullable() {
        return Optional.ofNullable(telemetryHooks);
    }
}
