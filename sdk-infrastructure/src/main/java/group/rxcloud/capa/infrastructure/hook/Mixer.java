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

/**
 * The Inner Runtimes Mixer.
 */
public abstract class Mixer {

    private static ConfigurationHooks configurationHooks;
    private static TelemetryHooks telemetryHooks;

    /**
     * Register configuration hooks.
     *
     * @param configurationHooks the configuration hooks
     */
    public static void registerConfigurationHooks(ConfigurationHooks configurationHooks) {
        Mixer.configurationHooks = configurationHooks;
    }

    /**
     * Register telemetry hooks.
     *
     * @param telemetryHooks the telemetry hooks
     */
    public static void registerTelemetryHooks(TelemetryHooks telemetryHooks) {
        Mixer.telemetryHooks = telemetryHooks;
    }

    /**
     * Gets configuration hooks.
     *
     * @return the configuration hooks
     */
    public static ConfigurationHooks getConfigurationHooks() {
        return configurationHooks;
    }

    /**
     * Gets telemetry hooks.
     *
     * @return the telemetry hooks
     */
    public static TelemetryHooks getTelemetryHooks() {
        return telemetryHooks;
    }
}
