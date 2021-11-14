package group.rxcloud.capa.spi.telemetry;

import group.rxcloud.capa.component.telemetry.trace.CapaTracerBuilder;
import io.opentelemetry.api.trace.TracerBuilder;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:15
 */
public abstract class CapaTracerBuilderSpi extends CapaTracerBuilder {

    public CapaTracerBuilderSpi(String tracerName, TracerBuilder builder) {
        super(tracerName, builder);
    }

}
