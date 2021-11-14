package group.rxcloud.capa.component.telemetry.trace.config;

import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.trace.SpanProcessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: chenyijiang
 * @date: 2021/11/4 11:44
 */
public class TracerConfig implements Serializable {

    private static final long serialVersionUID = 6587103489345563395L;

    private String idGenerator;

    private boolean enableIdValidate;

    private SamplerConfig sampler;

    private SpanLimitsConfig spanLimits;

    private List<String> processors;

    private List<String> contextPropagators;

    private transient List<SpanProcessor> processorsInstance;

    private transient List<TextMapPropagator> contextPropagatorsInstance;

    public List<SpanProcessor> getProcessorsInstance() {
        return processorsInstance;
    }

    public void setProcessorsInstance(List<SpanProcessor> processorsInstance) {
        this.processorsInstance = processorsInstance;
    }

    public List<TextMapPropagator> getContextPropagatorsInstance() {
        return contextPropagatorsInstance;
    }

    public void setContextPropagatorsInstance(
            List<TextMapPropagator> contextPropagatorsInstance) {
        this.contextPropagatorsInstance = contextPropagatorsInstance;
    }

    public List<String> getContextPropagators() {
        return contextPropagators;
    }

    public void setContextPropagators(List<String> contextPropagators) {
        this.contextPropagators = contextPropagators;
    }

    public String getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(String className) {
        this.idGenerator = className;
    }

    public boolean isEnableIdValidate() {
        return enableIdValidate;
    }

    public void setEnableIdValidate(boolean enableIdValidate) {
        this.enableIdValidate = enableIdValidate;
    }

    public SamplerConfig getSampler() {
        return sampler;
    }

    public void setSampler(SamplerConfig sampler) {
        this.sampler = sampler;
    }

    public SpanLimitsConfig getSpanLimits() {
        return spanLimits;
    }

    public void setSpanLimits(SpanLimitsConfig spanLimits) {
        this.spanLimits = spanLimits;
    }

    public List<String> getProcessors() {
        return processors;
    }

    public void setProcessors(List<String> processors) {
        this.processors = processors;
    }

    public void addProcessor(String processor) {
        if (processors == null) {
            processors = new ArrayList<>();
        }
        processors.add(processor);
    }

    public void addContextPropagator(String contextPropagator) {
        if (contextPropagators == null) {
            contextPropagators = new ArrayList<>();
        }
        contextPropagators.add(contextPropagator);
    }

    public void addProcessorInstance(SpanProcessor processor) {
        if (processorsInstance == null) {
            processorsInstance = new ArrayList<>();
        }
        processorsInstance.add(processor);
    }

    public void addContextPropagatorInstance(TextMapPropagator contextPropagator) {
        if (contextPropagatorsInstance == null) {
            contextPropagatorsInstance = new ArrayList<>();
        }
        contextPropagatorsInstance.add(contextPropagator);
    }
}
