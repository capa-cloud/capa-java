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
import group.rxcloud.capa.component.telemetry.context.ContextConfig;
import group.rxcloud.capa.component.telemetry.metrics.MeterConfig;
import group.rxcloud.capa.component.telemetry.metrics.MetricsReaderConfig;
import group.rxcloud.capa.component.telemetry.trace.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.TracerConfig;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.sdk.trace.IdGenerator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author: chenyijiang
 * @date: 2021/11/26 13:33
 */
public class CapaTelemetryClientBuilderTest {

    @Test
    public void build() {
        CapaTelemetryClient client = new CapaTelemetryClientBuilder().build();
        assertNotNull(client.getContextPropagators().block());
        assertNotNull(client.buildTracer("aaa").block());
        assertNotNull(client.buildTracer("aaa", "ccc", "fff").block());
        assertNotNull(client.buildTracer("aaa", "sss").block());
        assertNotNull(client.buildMeter("bbb").block());
        assertNotNull(client.buildMeter("bbb", "shdas", "dasoi").block());
        assertNotNull(client.buildMeter("bbb", "dhasiug").block());
    }

    @Test
    public void buildManual() throws InterruptedException {
        MetricsReaderConfig readerConfig = new MetricsReaderConfig();
        readerConfig.setExporterType(MetricTestExporter.class.getName());
        readerConfig.setName("metric-reader");
        readerConfig.setExportInterval(1, TimeUnit.SECONDS);
        CapaTelemetryClient capaTelemetryClient = new CapaTelemetryClientBuilder()
                .addProcessor(new TraceProcessor())
                .setSamplerConfig(() -> SamplerConfig.DEFAULT_CONFIG)
                .setSpanLimits(new SpanLimitsConfig())
                .setIdGenerator(IdGenerator.random())
                .setTracerConfig(new TracerConfig())
                .setMeterConfig(new MeterConfig())
                .addMetricReaderConfig(readerConfig)
                .setContextConfig(new ContextConfig())
                .addContextPropagators(W3CTraceContextPropagator.getInstance())
                .build();

        // tracer
        Tracer tracer = capaTelemetryClient.buildTracer("tracer-test")
                                           .block();

        LongCounter counter = capaTelemetryClient.buildMeter("meter-test")
                                                 .block()
                                                 .counterBuilder("counter-test")
                                                 .build();

        Span span = tracer.spanBuilder("span-test")
                          .setAttribute("key1", 1)
                          .setAttribute("key2", 2)
                          .startSpan();
        // working
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            counter.add(i);
        }

        span.end();
    }
}