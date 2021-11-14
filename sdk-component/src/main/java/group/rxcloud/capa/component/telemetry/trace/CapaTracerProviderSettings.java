package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.trace.config.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.util.List;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:48
 */
public interface CapaTracerProviderSettings {

    CapaTracerProviderSettings setTracerConfig(TracerConfig tracerConfig);

    CapaTracerProviderSettings setSpanLimits(SpanLimitsConfig spanLimits);

    CapaTracerProviderSettings setIdGenerator(IdGenerator idGenerator);

    CapaTracerProviderSettings setSampler(Sampler sampler);

    CapaTracerProviderSettings setProcessors(List<SpanProcessor> processors);

    CapaTracerProviderSettings addProcessor(SpanProcessor processor);

}
