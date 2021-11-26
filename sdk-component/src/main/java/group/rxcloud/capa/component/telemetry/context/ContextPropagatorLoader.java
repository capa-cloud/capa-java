package group.rxcloud.capa.component.telemetry.context;

import io.opentelemetry.context.propagation.ContextPropagators;

/**
 * Load default context propagator.
 */
public interface ContextPropagatorLoader {

    ContextPropagatorLoader DEFAULT = new ContextPropagatorLoader() {
    };

    /**
     * Load default context propagator.
     *
     * @return default context propagator.
     */
    default ContextPropagators load() {
        return ContextPropagators.noop();
    }
}
