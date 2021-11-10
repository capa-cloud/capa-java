package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessorBuilder;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.util.concurrent.TimeUnit;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:48
 */
public class CapaSpanExporterBuilder {

    private ExporterConfig config;

    public CapaSpanExporterBuilder(ExporterConfig config) {
        assert config != null;
        this.config = config;
    }

    public SpanProcessor build() {
        SpanExporter exporter = config.getExporterInstance();
        if (exporter == null) {
            throw new IllegalArgumentException("No Span Exporter was configured.");
        }
        if (config.isUseBatch()) {
            return buildSpanProcessor(config, exporter);
        } else {
            return SimpleSpanProcessor.create(exporter);
        }
    }

    private BatchSpanProcessor buildSpanProcessor(ExporterConfig config, SpanExporter exporter) {
        BatchSpanProcessorBuilder builder = BatchSpanProcessor.builder(exporter);
        if (config.getExporterTimeoutMillis() != null) {
            builder.setExporterTimeout(config.getExporterTimeoutMillis(), TimeUnit.MILLISECONDS);
        }
        if (config.getScheduleDelayMillis() != null) {
            builder.setScheduleDelay(config.getScheduleDelayMillis(), TimeUnit.MILLISECONDS);
        }
        if (config.getMaxQueueSize() != null) {
            builder.setMaxQueueSize(config.getMaxQueueSize());
        }
        if (config.getMaxExportBatchSize() != null) {
            builder.setMaxExportBatchSize(config.getMaxExportBatchSize());
        }
        return builder.build();
    }
}
