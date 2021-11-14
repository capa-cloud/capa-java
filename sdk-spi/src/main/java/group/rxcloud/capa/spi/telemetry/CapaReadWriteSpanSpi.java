package group.rxcloud.capa.spi.telemetry;

import group.rxcloud.capa.component.telemetry.trace.CapaReadWriteSpan;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.sdk.trace.ReadWriteSpan;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:15
 */
public abstract class CapaReadWriteSpanSpi extends CapaReadWriteSpan {

    public CapaReadWriteSpanSpi(ReadWriteSpan span) {
        super(span);
    }

}
