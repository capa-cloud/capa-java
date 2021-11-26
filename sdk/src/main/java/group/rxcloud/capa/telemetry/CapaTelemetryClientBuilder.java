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

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import group.rxcloud.capa.component.telemetry.context.CapaContextPropagatorBuilder;
import group.rxcloud.capa.component.telemetry.context.CapaContextPropagatorSettings;
import group.rxcloud.capa.component.telemetry.context.ContextConfig;
import group.rxcloud.capa.component.telemetry.metrics.CapaMeterProviderBuilder;
import group.rxcloud.capa.component.telemetry.metrics.CapaMeterProviderSettings;
import group.rxcloud.capa.component.telemetry.metrics.MeterConfig;
import group.rxcloud.capa.component.telemetry.metrics.MetricsReaderConfig;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderBuilder;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderSettings;
import group.rxcloud.capa.component.telemetry.trace.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.TracerConfig;
import group.rxcloud.capa.rpc.CapaRpcClient;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SpanProcessor;

/**
 * A builder for the {@link CapaTelemetryClient}
 */
public class CapaTelemetryClientBuilder implements CapaContextPropagatorSettings, CapaTracerProviderSettings, CapaMeterProviderSettings {

    private final CapaTracerProviderBuilder tracerProviderBuilder = new CapaTracerProviderBuilder();

    private final CapaMeterProviderBuilder meterProviderBuilder = new CapaMeterProviderBuilder();

    private final CapaContextPropagatorBuilder contextPropagatorBuilder = new CapaContextPropagatorBuilder();

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
    public CapaTelemetryClientBuilder addProcessor(SpanProcessor processor) {
        tracerProviderBuilder.addProcessor(processor);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setContextConfig(ContextConfig config) {
        contextPropagatorBuilder.setContextConfig(config);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder addContextPropagators(TextMapPropagator processor) {
        contextPropagatorBuilder.addContextPropagators(processor);
        return this;
    }


    @Override
    public CapaTelemetryClientBuilder setMeterConfig(MeterConfig config) {
        meterProviderBuilder.setMeterConfig(config);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder addMetricReaderConfig(MetricsReaderConfig config) {
        meterProviderBuilder.addMetricReaderConfig(config);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setSamplerConfig(SamplerConfig samplerConfig) {
        meterProviderBuilder.setSamplerConfig(samplerConfig);
        tracerProviderBuilder.setSamplerConfig(samplerConfig);
        return this;
    }

    public CapaTelemetryClient build() {
        CapaTelemetryClientGlobal client = new CapaTelemetryClientGlobal();

        // context
        client.setContextPropagators(contextPropagatorBuilder.buildContextPropagators());

        // tracer
        client.setTracerProvider(tracerProviderBuilder.buildTracerProvider());

        // meter
        client.setMeterProvider(meterProviderBuilder.buildMeterProvider());

        return client;
    }

}
