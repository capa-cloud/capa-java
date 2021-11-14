package group.rxcloud.capa.telemetry;

import group.rxcloud.capa.component.telemetry.trace.CapaContextPropagatorSettings;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderBuilder;
import group.rxcloud.capa.component.telemetry.trace.CapaTracerProviderSettings;
import group.rxcloud.capa.component.telemetry.trace.config.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.util.List;

/**
 * @author: chenyijiang
 * @date: 2021/11/4 10:55
 */
public class CapaTelemetryClientBuilder implements CapaContextPropagatorSettings, CapaTracerProviderSettings {

    private final CapaTracerProviderBuilder tracerProviderBuilder = new CapaTracerProviderBuilder();

    @Override
    public CapaTelemetryClientBuilder setTracerConfig(TracerConfig tracerConfig) {
        tracerProviderBuilder.setTracerConfig(tracerConfig);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setSpanLimits(SpanLimitsConfig spanLimits) {
        tracerProviderBuilder.setSpanLimits(spanLimits);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setIdGenerator(IdGenerator idGenerator) {
        tracerProviderBuilder.setIdGenerator(idGenerator);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setSampler(Sampler sampler) {
        tracerProviderBuilder.setSampler(sampler);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setProcessors(List<SpanProcessor> processors) {
        tracerProviderBuilder.setProcessors(processors);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder addProcessor(SpanProcessor processor) {
        tracerProviderBuilder.addProcessor(processor);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder setContextPropagators(List<TextMapPropagator> contextPropagators) {
        tracerProviderBuilder.setContextPropagators(contextPropagators);
        return this;
    }

    @Override
    public CapaTelemetryClientBuilder addContextPropagators(TextMapPropagator processor) {
        tracerProviderBuilder.addContextPropagators(processor);
        return this;
    }

    public CapaTelemetryClient build() {
        CapaTelemetryClientGlobal client = new CapaTelemetryClientGlobal();

        // context
        client.setContextPropagators(tracerProviderBuilder.buildContextPropagators());

        // tracer
        client.setTracerProvider(tracerProviderBuilder.buildTracerProvider());

        // todo add other components

        GlobalOpenTelemetry.set(client);

        return client;
    }

}
