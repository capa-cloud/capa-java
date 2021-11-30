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

import group.rxcloud.cloudruntimes.domain.enhanced.TelemetryRuntimes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.ContextPropagators;
import reactor.core.publisher.Mono;

/**
 * The Mixer telemetry hooks.
 */
public interface TelemetryHooks extends TelemetryRuntimes {

    @Override
    default Mono<Tracer> buildTracer(String tracerName) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Tracer> buildTracer(String tracerName, String version) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Tracer> buildTracer(String tracerName, String version, String schemaUrl) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<ContextPropagators> getContextPropagators() {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Meter> buildMeter(String meterName) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Meter> buildMeter(String meterName, String version) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }

    @Override
    default Mono<Meter> buildMeter(String meterName, String version, String schemaUrl) {
        throw new UnsupportedOperationException("If you want to use this operate, please impl this.");
    }
}
