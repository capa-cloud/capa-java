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

import group.rxcloud.capa.component.telemetry.trace.config.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.util.List;

/**
 */
public interface CapaTracerProviderSettings {

    CapaTracerProviderSettings setTracerConfig(TracerConfig tracerConfig);

    CapaTracerProviderSettings setSpanLimits(SpanLimitsConfig spanLimits);

    CapaTracerProviderSettings setIdGenerator(IdGenerator idGenerator);

    CapaTracerProviderSettings setSampler(Sampler sampler);

    CapaTracerProviderSettings setProcessors(List<SpanProcessor> processors);

    CapaTracerProviderSettings addProcessor(SpanProcessor processor);

}
