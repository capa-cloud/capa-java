package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerBuilder;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:06
 */
public class CapaTracerBuilder implements TracerBuilder {

    protected final TracerBuilder tracerBuilder;

    protected final String tracerName;

    public CapaTracerBuilder(String tracerName, TracerBuilder tracerBuilder) {
        this.tracerName = tracerName;
        this.tracerBuilder = tracerBuilder;
    }

    @Override
    public TracerBuilder setSchemaUrl(String schemaUrl) {
        tracerBuilder.setSchemaUrl(schemaUrl);
        return this;
    }

    @Override
    public TracerBuilder setInstrumentationVersion(String instrumentationVersion) {
        tracerBuilder.setInstrumentationVersion(instrumentationVersion);
        return this;
    }

    @Override
    public Tracer build() {
        return CapaWrapper.wrap(tracerName, tracerBuilder.build());
    }
}
