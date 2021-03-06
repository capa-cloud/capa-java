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

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.context.propagation.ContextPropagators;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class CapaTelemetryClientGlobal implements CapaTelemetryClient, OpenTelemetry {

    // noop as default.
    private TracerProvider tracerProvider = TracerProvider.noop();

    private MeterProvider meterProvider = MeterProvider.noop();

    private ContextPropagators contextPropagators = ContextPropagators.noop();

    protected List<String> registryNames;

    @Override
    public List<String> registryNames() {
        return registryNames;
    }

    CapaTelemetryClientGlobal() {
        this.registryNames = new ArrayList<>(1);
        this.registryNames.add("opentelemetry");
    }

    @Override
    public TracerProvider getTracerProvider() {
        return tracerProvider;
    }

    void setTracerProvider(TracerProvider tracerProvider) {
        this.tracerProvider = tracerProvider;
    }

    public MeterProvider getMeterProvider() {
        return meterProvider;
    }

    void setMeterProvider(MeterProvider meterProvider) {
        this.meterProvider = meterProvider;
    }

    @Override
    public ContextPropagators getPropagators() {
        return contextPropagators;
    }

    @Override
    public Mono<Tracer> buildTracer(String tracerName) {
        return Mono.fromSupplier(() ->
                tracerProvider.tracerBuilder(tracerName)
                        .build());
    }

    @Override
    public Mono<ContextPropagators> getContextPropagators() {
        return Mono.fromSupplier(() -> contextPropagators);
    }

    void setContextPropagators(ContextPropagators contextPropagators) {
        this.contextPropagators = contextPropagators;
    }

    @Override
    public Mono<Tracer> buildTracer(String tracerName, String version) {
        return Mono.fromSupplier(() ->
                tracerProvider.tracerBuilder(tracerName)
                        .setInstrumentationVersion(version)
                        .build());
    }

    @Override
    public Mono<Tracer> buildTracer(String tracerName, String version, String schemaUrl) {
        return Mono.fromSupplier(() ->
                tracerProvider.tracerBuilder(tracerName)
                        .setInstrumentationVersion(version)
                        .setSchemaUrl(schemaUrl)
                        .build());
    }

    @Override
    public Mono<Meter> buildMeter(String meterName) {
        return Mono.fromSupplier(() ->
                meterProvider.meterBuilder(meterName)
                        .build());
    }

    @Override
    public Mono<Meter> buildMeter(String meterName, String version) {
        return Mono.fromSupplier(() ->
                meterProvider.meterBuilder(meterName)
                        .setInstrumentationVersion(version)
                        .build());
    }

    @Override
    public Mono<Meter> buildMeter(String meterName, String version, String schemaUrl) {
        return Mono.fromSupplier(() ->
                meterProvider.meterBuilder(meterName)
                        .setInstrumentationVersion(version)
                        .setSchemaUrl(schemaUrl)
                        .build());
    }

    @Override
    public void close() {
    }
}
