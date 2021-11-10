package group.rxcloud.capa.examples.telemetry;

import group.rxcloud.capa.component.telemetry.ExporterConfig;
import group.rxcloud.capa.component.telemetry.SamplerConfig;
import group.rxcloud.capa.component.telemetry.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.TracerConfig;
import group.rxcloud.capa.telemetry.CapaTelemetryClient;
import group.rxcloud.capa.telemetry.CapaTelemetryClientBuilder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: chenyijiang
 * @date: 2021/11/5 14:48
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

    public static void main(String[] args) throws InterruptedException {
        DemoTelemetryClient demo = new DemoTelemetryClient();
        demo.logTraceWithManualSetParentSpan();

        for (int i = 0; i < 1; i++) {
            demo.logTraceWithTryCatchToCloseTheScopeResource();
        }

        Thread.sleep(5 * 1000);
    }

    private void work() {
        Span current = Span.current();
        StringBuilder builder = new StringBuilder();
        builder.append('\n')
               .append("============================== Main Func Working =======================================\n")
               .append("Thread: " + Thread.currentThread().getName())
               .append('\n')
               .append("Current trace id is: " + current.getSpanContext().getTraceId())
               .append('\n')
               .append("Current span id is: " + current.getSpanContext().getSpanId())
               .append('\n')
               .append("============================== Main Func Working =======================================\n");
        System.out.println(builder);
    }

    /**
     * 用 setParent 方法定义Span的父子关系，无法从Context中获取当前的Span。
     *
     * @throws InterruptedException
     */
    private void logTraceWithManualSetParentSpan() throws InterruptedException {
        TracerConfig tracerConfig = buildConfig();

        CapaTelemetryClient client = new CapaTelemetryClientBuilder(tracerConfig).build();
        Tracer tracer = client.getTracer("demo-tracer").block();

        // setNoParent后，会新建一个traceId，否则会使用当前上下文的traceId
        Span rootSpan = tracer.spanBuilder("RootSpan")
                              .setAttribute("level", 1)
                              .startSpan();

        // 跨线程trace串联。
        ExecutorService traceableExecutorService = Context.current().wrap(EXEXUTOR_SERVICE);
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            traceableExecutorService.submit(() -> {
                Span innerSpan1 = tracer.spanBuilder("InnerSpan1_id_" + finalI)
                                        .setAttribute("level", 2)
                                        .setAttribute("id", finalI)
                                        .setParent(Context.current().with(rootSpan))
                                        .startSpan();
                // do something
                work();
                innerSpan1.end();

                Span innerSpan2 = tracer.spanBuilder("InnerSpan2_id_" + finalI)
                                        .setAttribute("level", 2)
                                        .setAttribute("id", finalI)
                                        .setParent(Context.current().with(rootSpan))
                                        .startSpan();

                SpanBuilder innerSubSpan2Builder = tracer.spanBuilder("InnerSpan2_sub1_id_" + finalI)
                                                         .setParent(Context.current().with(innerSpan2))
                                                         .setAttribute("level", 3)
                                                         .setAttribute("id", finalI);
                // 过长的 attrbutes value，会被substring
                innerSubSpan2Builder.setAttribute("longAttr", "012345678901234567890123456789");
                // 过多的 attrbutes会被丢弃
                for (int j = 0; j < 20; j++) {
                    innerSubSpan2Builder.setAttribute("attr" + j, j);
                }

                Span innerSubSpan2 = innerSubSpan2Builder.startSpan();
                // do something
                work();
                innerSubSpan2.end();
                innerSpan2.end();

            });
        }

        rootSpan.end();

    }

    /**
     * 用 makeCurrent 方法规范Span的父子关系，可以从Context中获取当前的Span。
     * 但Scope资源在span结束时必需close，才能在子Span结束时将父Span恢复到上下文中。
     *
     * @throws InterruptedException
     */
    private void logTraceWithTryCatchToCloseTheScopeResource() throws InterruptedException {
        TracerConfig tracerConfig = buildConfig();

        CapaTelemetryClient client = new CapaTelemetryClientBuilder(tracerConfig).build();
        Tracer tracer = client.getTracer("demo-tracer").block();

        // setNoParent后，会新建一个traceId，否则会使用当前上下文的traceId
        Span rootSpan = tracer.spanBuilder("RootSpan")
                              .setAttribute("level", 1).startSpan();

        // scope 需要 close，避免内存泄漏
        try (Scope rootScope = rootSpan.makeCurrent()) {
            // 跨线程trace串联。
             ExecutorService traceableExecutorService = Context.current().wrap(EXEXUTOR_SERVICE);
            for (int i = 0; i < 2; i++) {
                int finalI = i;
                 traceableExecutorService.submit(() -> {
                    Span innerSpan1 = tracer.spanBuilder("InnerSpan1")
                                            .setAttribute("level", 2)
                                            .setAttribute("id", finalI)
                                            .startSpan();
                    try (Scope inner1 = innerSpan1.makeCurrent()){
                        // do something
                        work();
                    } finally {
                        innerSpan1.end();
                    }

                    Span innerSpan2 = tracer.spanBuilder("InnerSpan2")
                                            .setAttribute("level", 2).setAttribute("id", finalI).startSpan();
                    try (Scope inner2 = innerSpan2.makeCurrent()){
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
                        try (Scope innerSub2 = innerSubSpan2.makeCurrent()){
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

    private TracerConfig buildConfig() {
        // default id generator (random number)
        TracerConfig config = new TracerConfig();

        // 取样率50%，每秒最多5条
        SamplerConfig samplerConfig = new SamplerConfig();
        samplerConfig.setMaxSampleCount(5);
        samplerConfig.setInterval(10L, TimeUnit.MINUTES);
        samplerConfig.setSampleRatio(0.5);
        config.setSampler(samplerConfig);

        SpanLimitsConfig spanLimitsConfig = new SpanLimitsConfig();
        spanLimitsConfig.setMaxNumAttributes(10);
        spanLimitsConfig.setMaxAttributeValueLength(20);
        config.setSpanLimits(spanLimitsConfig);

        // 单条Exporter
        ExporterConfig exporterConfig1 = new ExporterConfig();
        exporterConfig1.setExporterClass(ConsoleSpanExporter.class.getCanonicalName());
        exporterConfig1.setConstructArgs("MySimpleExporter", true);

        // 批量Exporter，单次export最多3条，schedule间隔2s
        ExporterConfig exporterConfig2 = new ExporterConfig();
        exporterConfig2.setExporterInstance(new ConsoleSpanExporter("MyBatchExporter", true));
        exporterConfig2.setUseBatch(true);
        exporterConfig2.setMaxExportBatchSize(3);
        exporterConfig2.setScheduleDelay(2L, TimeUnit.SECONDS);

        //config.addExporter(exporterConfig1);
        config.addExporter(exporterConfig2);

        return config;
    }


}
