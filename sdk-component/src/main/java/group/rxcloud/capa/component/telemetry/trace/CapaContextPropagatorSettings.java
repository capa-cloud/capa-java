package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.context.propagation.TextMapPropagator;

import java.util.List;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:48
 */
public interface CapaContextPropagatorSettings {

    CapaContextPropagatorSettings setContextPropagators(List<TextMapPropagator> contextPropagators);

    CapaContextPropagatorSettings addContextPropagators(TextMapPropagator processor);

}
