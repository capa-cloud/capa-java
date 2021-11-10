package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.sdk.trace.*;

import java.util.List;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:48
 */
public class SdkTracerBuilder {

    private TracerConfig tracerConfig;

    public SdkTracerBuilder(TracerConfig tracerConfig) {
        assert tracerConfig != null;
        this.tracerConfig = tracerConfig;
    }

    public SdkTracerProvider build() {
        SdkTracerProviderBuilder builder = SdkTracerProvider.builder();

        IdGenerator idGenerator = tracerConfig.getIdGeneratorInstance();
        if (idGenerator != null) {
            builder.setIdGenerator(idGenerator);
        }

        SpanLimitsConfig spanLimits = tracerConfig.getSpanLimits();
        if (spanLimits != null) {
            SpanLimitsBuilder limits = SpanLimits.builder();
            if (spanLimits.getMaxAttributeValueLength() != null) {
                limits.setMaxAttributeValueLength(spanLimits.getMaxAttributeValueLength());
            }
            if (spanLimits.getMaxNumAttributes() != null) {
                limits.setMaxNumberOfAttributes(spanLimits.getMaxNumAttributes());
            }
            if (spanLimits.getMaxNumEvents() != null) {
                limits.setMaxNumberOfEvents(spanLimits.getMaxNumEvents());
            }
            if (spanLimits.getMaxNumLinks() != null) {
                limits.setMaxNumberOfLinks(spanLimits.getMaxNumLinks());
            }
            if (spanLimits.getMaxNumAttributesPerLink() != null) {
                limits.setMaxNumberOfAttributesPerLink(spanLimits.getMaxNumAttributesPerLink());
            }
            if (spanLimits.getMaxNumAttributesPerEvent() != null) {
                limits.setMaxNumberOfAttributesPerEvent(spanLimits.getMaxNumAttributesPerEvent());
            }
            builder.setSpanLimits(limits.build());
        }

        SamplerConfig sampler = tracerConfig.getSampler();
        if (sampler != null) {
            builder.setSampler(CapaSamplerProvider.getOrCreate(sampler));
        }

        List<ExporterConfig> exporterConfigs = tracerConfig.getExporters();
        if (exporterConfigs != null) {
            exporterConfigs.forEach(e -> {
                builder.addSpanProcessor(new CapaSpanExporterBuilder(e).build());
            });
        }

        return builder.build();
    }
}
