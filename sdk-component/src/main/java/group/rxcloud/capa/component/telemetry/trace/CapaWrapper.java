package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.SpiUtils;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerBuilder;
import io.opentelemetry.sdk.trace.ReadWriteSpan;

import javax.annotation.Nullable;

/**
 * Load capa implementation.
 *
 * @author: chenyijiang
 * @date: 2021/11/11 21:20
 */
final class CapaWrapper {

    private CapaWrapper() {
    }

    @Nullable
    static CapaSpanBuilder wrap(String tracerName, String spanName, SpanBuilder builder) {
        CapaSpanBuilder result = SpiUtils
                .getFromSpiConfigFile(CapaSpanBuilder.class, new Class[]{String.class, String.class, SpanBuilder.class},
                        new Object[]{tracerName, spanName, builder});
        if (result == null) {
            result = new CapaSpanBuilder(tracerName, spanName, builder);
        }
        return result;
    }

    @Nullable
    static CapaReadWriteSpan wrap(ReadWriteSpan span) {
        CapaReadWriteSpan result = SpiUtils
                .getFromSpiConfigFile(CapaReadWriteSpan.class, new Class[]{ReadWriteSpan.class}, new Object[]{span});
        if (result == null) {
            result = new CapaReadWriteSpan(span);
        }
        return result;
    }

    @Nullable
    static CapaTracer wrap(String tracerName, Tracer tracer) {
        CapaTracer result = SpiUtils.getFromSpiConfigFile(CapaTracer.class, new Class[]{String.class, Tracer.class},
                new Object[]{tracerName, tracer});
        if (result == null) {
            result = new CapaTracer(tracerName, tracer);
        }
        return result;
    }

    @Nullable
    static CapaTracerBuilder wrap(String tracerName, TracerBuilder tracerBuilder) {
        CapaTracerBuilder result = SpiUtils
                .getFromSpiConfigFile(CapaTracerBuilder.class, new Class[]{String.class, TracerBuilder.class},
                        new Object[]{tracerName, tracerBuilder});
        if (result == null) {
            result = new CapaTracerBuilder(tracerName, tracerBuilder);
        }
        return result;
    }

}
