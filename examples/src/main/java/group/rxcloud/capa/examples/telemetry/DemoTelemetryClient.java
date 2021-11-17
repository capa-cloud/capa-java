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
package group.rxcloud.capa.examples.telemetry;

import com.fasterxml.jackson.core.JsonProcessingException;
import group.rxcloud.capa.component.telemetry.CapaContext;
import group.rxcloud.capa.component.telemetry.trace.config.SamplerConfig;
import group.rxcloud.capa.component.telemetry.trace.config.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;
import group.rxcloud.capa.telemetry.CapaTelemetryClient;
import group.rxcloud.capa.telemetry.CapaTelemetryClientBuilder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class DemoTelemetryClient {

    private static AtomicInteger WORKER_COUNT = new AtomicInteger();

    private static ExecutorService EXEXUTOR_SERVICE = Executors.newFixedThreadPool(3, new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("worker_" + WORKER_COUNT.incrementAndGet());
            return thread;
        }
    });

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

//        CapaTelemetryClient client = buildFromConfig(false);
        CapaTelemetryClient client = buildFromConfigFile();
//        CapaTelemetryClient client = buildFromBuilder();
        for (int i = 0; i < 6; i++) {
            logDemo(client);
        }

        Thread.sleep(5 * 1000);
    }

    /**
     * 用 makeCurrent 方法规范Span的父子关系，可以从Context中获取当前的Span。
     * 但Scope资源在span结束时必需close，才能在子Span结束时将父Span恢复到上下文中。
     *
     * @throws InterruptedException
     */
    private static void logDemo(CapaTelemetryClient client) throws InterruptedException {
        Tracer tracer = client.buildTracer("demo-tracer").block();

        // setNoParent后，会新建一个traceId，否则会使用当前上下文的traceId
        Span rootSpan = tracer.spanBuilder("RootSpan")
                              .setAttribute("level", 1)
                              .startSpan();

        // scope 需要 close
        try (Scope rootScope = rootSpan.makeCurrent()) {
            // 跨线程trace串联。
            ExecutorService traceableExecutorService = CapaContext.taskWrapping(EXEXUTOR_SERVICE);
            for (int i = 0; i < 2; i++) {
                int finalI = i;
                traceableExecutorService.submit(() -> {
                    Span innerSpan1 = tracer.spanBuilder("InnerSpan1")
                                            .setAttribute("level", 2)
                                            .setAttribute("id", finalI)
                                            .startSpan();
                    try (Scope inner1 = innerSpan1.makeCurrent()) {
                        // do something
                        work();
                    } finally {
                        innerSpan1.end();
                    }

                    Span innerSpan2 = tracer.spanBuilder("InnerSpan2")
                                            .setAttribute("level", 2).setAttribute("id", finalI).startSpan();
                    try (Scope inner2 = innerSpan2.makeCurrent()) {
                        // 调用 makeCurrent后，innerSubSpan2的父span会变成innerSpan2

                        SpanBuilder innerSubSpan2Builder = tracer.spanBuilder("InnerSpan2_sub1")
                                                                 .setAttribute("level", 3).setAttribute("id", finalI);
                        // 过长的 attrbutes value，会被substring
                        innerSubSpan2Builder.setAttribute("longAttr", "012345678901234567890123456789");
                        // 过多的 attrbutes会被丢弃
                        for (int j = 0; j < 20; j++) {
                            innerSubSpan2Builder.setAttribute("attr" + j, j);
                        }

                        Span innerSubSpan2 = innerSubSpan2Builder.startSpan();
                        try (Scope innerSub2 = innerSubSpan2.makeCurrent()) {
                            // do something
                            work();
                        } finally {
                            innerSubSpan2.end();
                        }

                    } finally {
                        innerSpan2.end();
                    }

                });
            }

        } finally {
            rootSpan.end();
        }

    }

    private static void work() {
//        Span current = Span.current();
//        StringBuilder builder = new StringBuilder();
//        builder.append('\n')
//               .append("============================== Main Func Working =======================================\n")
//               .append("Thread: " + Thread.currentThread().getName())
//               .append('\n')
//               .append("Current trace id is: " + current.getSpanContext().getTraceId())
//               .append('\n')
//               .append("Current span id is: " + current.getSpanContext().getSpanId())
//               .append('\n')
//               .append("============================== Main Func Working =======================================\n");
//        System.out.println(builder);
    }

    private static CapaTelemetryClient buildFromConfig(boolean disable) {
        TracerConfig tracerConfig = new TracerConfig();

        SpanLimitsConfig spanLimitsConfig = new SpanLimitsConfig();
        spanLimitsConfig.setMaxNumAttributes(10);
        spanLimitsConfig.setMaxAttributeValueLength(20);
        tracerConfig.setSpanLimits(spanLimitsConfig);

        SamplerConfig samplerConfig = new SamplerConfig();
        samplerConfig.setDisable(disable);
        samplerConfig.setName("MySampler");
        tracerConfig.setSampler(samplerConfig);

        tracerConfig.addProcessor(ConsoleSpanProcessor.class.getName());

        return new CapaTelemetryClientBuilder().setTracerConfig(tracerConfig).build();
    }

    private static CapaTelemetryClient buildFromBuilder() throws JsonProcessingException {
        SpanLimitsConfig spanLimitsConfig = new SpanLimitsConfig();
        spanLimitsConfig.setMaxNumAttributes(10);
        spanLimitsConfig.setMaxAttributeValueLength(20);

        return new CapaTelemetryClientBuilder().setIdGenerator(IdGenerator.random())
                                               .setSampler(Sampler.alwaysOn())
                                               .setSpanLimits(spanLimitsConfig)
                                               .addProcessor(SimpleSpanProcessor.create(new ConsoleSpanExporter("MyExporter", true)))
                                               .build();
    }

    private static CapaTelemetryClient buildFromConfigFile() throws JsonProcessingException {
        // file: capa-tracer.json in directory 'resources'
        return new CapaTelemetryClientBuilder().build();
    }

}
