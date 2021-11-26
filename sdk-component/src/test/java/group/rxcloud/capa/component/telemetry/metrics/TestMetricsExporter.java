package group.rxcloud.capa.component.telemetry.metrics;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;

import java.util.Collection;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 17:04
 */
public class TestMetricsExporter implements MetricExporter {

    @Override
    public CompletableResultCode export(Collection<MetricData> metrics) {
        metrics.forEach(System.out::println);
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
    }
}
