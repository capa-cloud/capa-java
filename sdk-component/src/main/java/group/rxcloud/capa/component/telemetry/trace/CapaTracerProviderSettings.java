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
package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SpanProcessor;

/**
 * Settings for capa trace provider.
 */
public interface CapaTracerProviderSettings {

    String FILE_PATH = "/capa-tracer.json";

    /**
     * Replace the whole config for the meter.
     *
     * @param tracerConfig tracer config
     * @return current settings.
     */
    CapaTracerProviderSettings setTracerConfig(TracerConfig tracerConfig);

    /**
     * Set the span limits.
     *
     * @param spanLimits span limits config.
     * @return current settings.
     */
    CapaTracerProviderSettings setSpanLimits(SpanLimitsConfig spanLimits);

    /**
     * Set the trace/span id generator.
     *
     * @param idGenerator trace/span id generator.
     * @return current settings.
     */
    CapaTracerProviderSettings setIdGenerator(IdGenerator idGenerator);

    /**
     * Add one more span processor to current meter config.
     *
     * @param processor span processor.
     * @return current settings.
     */
    CapaTracerProviderSettings addProcessor(SpanProcessor processor);

    /**
     * Set sample config.
     *
     * @param samplerConfig sample config.
     * @return current settings.
     */
    CapaTracerProviderSettings setSamplerConfig(SamplerConfig samplerConfig);

}
