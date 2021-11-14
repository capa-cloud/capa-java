package group.rxcloud.capa.spi.telemetry;

import group.rxcloud.capa.component.telemetry.trace.CapaTracer;
import io.opentelemetry.api.trace.Tracer;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:15
 */
public abstract class CapaTracerSpi extends CapaTracer {

    public CapaTracerSpi(String name, Tracer tracer) {
        super(name, tracer);
    }

}
