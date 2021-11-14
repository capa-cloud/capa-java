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
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.context.propagation.ContextPropagators;
import reactor.core.publisher.Mono;

public class CapaTelemetryClientGlobal implements CapaTelemetryClient, OpenTelemetry {

    private TracerProvider tracerProvider = TracerProvider.noop();

    private ContextPropagators contextPropagators = ContextPropagators.noop();

    void setTracerProvider(TracerProvider tracerProvider) {
        this.tracerProvider = tracerProvider;
    }

    void setContextPropagators(ContextPropagators contextPropagators) {
        this.contextPropagators = contextPropagators;
    }

    @Override
    public TracerProvider getTracerProvider() {
        return tracerProvider;
    }

    @Override
    public ContextPropagators getPropagators() {
        return contextPropagators;
    }

    @Override
    public Mono<Tracer> buildTracer(String tracerName) {
        return Mono.fromSupplier(() -> {
            return tracerProvider.tracerBuilder(tracerName).build();
        });
    }

    @Override
    public Mono<ContextPropagators> getContextPropagators() {
        return Mono.fromSupplier(() -> {
            return contextPropagators;
        });
    }

    @Override
    public void close() {
        // todo
    }
}
