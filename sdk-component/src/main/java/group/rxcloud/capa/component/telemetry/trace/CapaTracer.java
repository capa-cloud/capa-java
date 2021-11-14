package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:06
 */
public class CapaTracer implements Tracer {

    protected final String tracerName;
    protected final Tracer tracer;

    public CapaTracer(String tracerName, Tracer tracer) {
        this.tracerName = tracerName;
        this.tracer = tracer;
    }

    @Override
    public SpanBuilder spanBuilder(String spanName) {
        SpanBuilder builder = tracer.spanBuilder(spanName);
        return CapaWrapper.wrap(tracerName, spanName, builder);
    }

}
