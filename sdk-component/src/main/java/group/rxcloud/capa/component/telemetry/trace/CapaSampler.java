package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.trace.config.SamplerConfig;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;

/**
 * todo 后续添加取样率相关参数
 *
 * @author: chenyijiang
 * @date: 2021/11/3 20:48
 */
public class CapaSampler implements Sampler {

    private Sampler inner;

    public CapaSampler(SamplerConfig config) {
        update(config);
    }

    public void update(SamplerConfig config) {
        if (config.isDisable()) {
            inner = Sampler.alwaysOff();
        } else {
            inner = Sampler.alwaysOn();
        }
        // todo more.
    }

    @Override
    public SamplingResult shouldSample(Context context, String traceId, String name, SpanKind kind,
                                       Attributes attributes, List<LinkData> list) {
        return inner.shouldSample(context, traceId, name, kind, attributes, list);
    }

    @Override
    public String getDescription() {
        return "Always or never sample the telemetry data.";
    }
}
