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
package group.rxcloud.capa.telemetry;

import group.rxcloud.capa.component.telemetry.trace.CapaContextPropagatorSettings;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderBuilder;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderSettings;
import group.rxcloud.capa.component.telemetry.trace.config.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.util.List;

/**
 */
public class CapaTelemetryClientBuilder implements CapaContextPropagatorSettings, CapaTracerProviderSettings {

    private final CapaTracerProviderBuilder tracerProviderBuilder = new CapaTracerProviderBuilder();

    @Override
    public CapaTelemetryClientBuilder setTracerConfig(TracerConfig tracerConfig) {
        tracerProviderBuilder.setTracerConfig(tracerConfig);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setSpanLimits(SpanLimitsConfig spanLimits) {
        tracerProviderBuilder.setSpanLimits(spanLimits);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setIdGenerator(IdGenerator idGenerator) {
        tracerProviderBuilder.setIdGenerator(idGenerator);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setSampler(Sampler sampler) {
        tracerProviderBuilder.setSampler(sampler);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setProcessors(List<SpanProcessor> processors) {
        tracerProviderBuilder.setProcessors(processors);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder addProcessor(SpanProcessor processor) {
        tracerProviderBuilder.addProcessor(processor);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setContextPropagators(List<TextMapPropagator> contextPropagators) {
        tracerProviderBuilder.setContextPropagators(contextPropagators);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder addContextPropagators(TextMapPropagator processor) {
        tracerProviderBuilder.addContextPropagators(processor);
        return this;
    }

    public CapaTelemetryClient build() {
        CapaTelemetryClientGlobal client = new CapaTelemetryClientGlobal();

        // context
        client.setContextPropagators(tracerProviderBuilder.buildContextPropagators());

        // tracer
        client.setTracerProvider(tracerProviderBuilder.buildTracerProvider());

        GlobalOpenTelemetry.set(client);
        CapaTelemetryClientGlobal.set(client);

        return client;
    }

}
