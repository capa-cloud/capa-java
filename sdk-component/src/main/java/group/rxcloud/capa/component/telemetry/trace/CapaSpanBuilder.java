package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;

import java.util.concurrent.TimeUnit;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:09
 */
public class CapaSpanBuilder implements SpanBuilder {

    protected final String tracerName;

    protected final String spanName;

    protected final SpanBuilder spanBuilder;

    public CapaSpanBuilder(String tracerName, String spanName, SpanBuilder builder) {
        this.tracerName = tracerName;
        this.spanName = spanName;
        spanBuilder = builder;
    }

    @Override
    public SpanBuilder setParent(Context context) {
        spanBuilder.setParent(context);
        return this;
    }

    @Override
    public SpanBuilder setNoParent() {
        spanBuilder.setNoParent();
        return this;
    }

    @Override
    public SpanBuilder addLink(SpanContext spanContext) {
        spanBuilder.addLink(spanContext);
        return this;
    }

    @Override
    public SpanBuilder addLink(SpanContext spanContext, Attributes attributes) {
        spanBuilder.addLink(spanContext, attributes);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, String value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, long value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, double value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, boolean value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public <T> SpanBuilder setAttribute(AttributeKey<T> key, T value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setSpanKind(SpanKind spanKind) {
        spanBuilder.setSpanKind(spanKind);
        return this;
    }

    @Override
    public SpanBuilder setStartTimestamp(long startTimestamp, TimeUnit unit) {
        spanBuilder.setStartTimestamp(startTimestamp, unit);
        return this;
    }

    @Override
    public Span startSpan() {
        Span span = spanBuilder.startSpan();
        if (span instanceof ReadWriteSpan) {
            return CapaWrapper.wrap((ReadWriteSpan) span);
        }
        return span;
    }
}
