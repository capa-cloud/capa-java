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

import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.ContextPropagators;
import reactor.core.publisher.Mono;

public interface CapaTelemetryClient extends DefaultCloudRuntimesClient {

    @Override
    Mono<Tracer> buildTracer(String tracerName);

    @Override
    Mono<ContextPropagators> getContextPropagators();

    @Override
    Mono<Tracer> buildTracer(String tracerName, String version);

    @Override
    Mono<Tracer> buildTracer(String tracerName, String version, String schemaUrl);

    @Override
    Mono<Meter> buildMeter(String meterName);

    @Override
    Mono<Meter> buildMeter(String meterName, String version);

    @Override
    Mono<Meter> buildMeter(String meterName, String version, String schemaUrl);

    @Override
    void close();
}
