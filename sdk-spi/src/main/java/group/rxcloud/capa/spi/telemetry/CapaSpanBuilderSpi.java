package group.rxcloud.capa.spi.telemetry;

import group.rxcloud.capa.component.telemetry.trace.CapaSpanBuilder;
import io.opentelemetry.api.trace.SpanBuilder;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:15
 */
public abstract class CapaSpanBuilderSpi extends CapaSpanBuilder {

    public CapaSpanBuilderSpi(String tracerName, String spanName, SpanBuilder builder) {
        super(tracerName, spanName, builder);
    }

}
