package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerBuilder;
import io.opentelemetry.api.trace.TracerProvider;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:06
 */
public class CapaTracerProvider implements TracerProvider {

    private final TracerProvider provider;

    public CapaTracerProvider(TracerProvider provider) {
        this.provider = provider;
    }

    @Override
    public Tracer get(String instrumentationName) {
        return provider.get(instrumentationName);
    }

    @Override
    public Tracer get(String instrumentationName, String instrumentationVersion) {
        return provider.get(instrumentationName, instrumentationVersion);
    }

    @Override
    public TracerBuilder tracerBuilder(String instrumentationName) {
        TracerBuilder tracerBuilder = provider.tracerBuilder(instrumentationName);
        return CapaWrapper.wrap(instrumentationName, tracerBuilder);
    }
}
