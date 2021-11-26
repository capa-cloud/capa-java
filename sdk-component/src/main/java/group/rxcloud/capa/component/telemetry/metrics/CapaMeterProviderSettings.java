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
package group.rxcloud.capa.component.telemetry.metrics;

import group.rxcloud.capa.component.telemetry.SamplerConfig;

/**
 * Settings for capa meter provider.
 */
public interface CapaMeterProviderSettings {

    String FILE_PATH ="/capa-meter.json";

    /**
     * Replace the whole config for the meter.
     *
     * @param config meter config.
     * @return current settings.
     */
    CapaMeterProviderSettings setMeterConfig(MeterConfig config);

    /**
     * Add one more reader to current meter config.
     *
     * @param config metrics reader config.
     * @return current settings.
     */
    CapaMeterProviderSettings addMetricReaderConfig(MetricsReaderConfig config);

    /**
     * Set sample config.
     *
     * @param samplerConfig sample config.
     * @return current settings.
     */
    CapaMeterProviderSettings setSamplerConfig(SamplerConfig samplerConfig);
}
