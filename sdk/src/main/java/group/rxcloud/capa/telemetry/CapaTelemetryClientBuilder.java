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

import group.rxcloud.capa.component.telemetry.context.CapaContextPropagatorBuilder;
import group.rxcloud.capa.component.telemetry.metrics.CapaMeterProviderBuilder;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderBuilder;

/**
 * A builder for the {@link CapaTelemetryClient}
 */
public class CapaTelemetryClientBuilder {

    /**
     * Bind to {@code telemetry} component
     */
    private final CapaTracerProviderBuilder tracerProviderBuilder;
    private final CapaMeterProviderBuilder meterProviderBuilder;
    private final CapaContextPropagatorBuilder contextPropagatorBuilder;

    public CapaTelemetryClientBuilder() {
        tracerProviderBuilder = new CapaTracerProviderBuilder();
        meterProviderBuilder = new CapaMeterProviderBuilder();
        contextPropagatorBuilder = new CapaContextPropagatorBuilder();
    }

    public CapaTelemetryClient build() {
        CapTelemetryClientExporter client = new CapTelemetryClientExporter();
        // context
        client.setContextPropagators(contextPropagatorBuilder.buildContextPropagators());
        // tracer
        client.setTracerProvider(tracerProviderBuilder.buildTracerProvider());
        // meter
        client.setMeterProvider(meterProviderBuilder.buildMeterProvider());
        return client;
    }
}
