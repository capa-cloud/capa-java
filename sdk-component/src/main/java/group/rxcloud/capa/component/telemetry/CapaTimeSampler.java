package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * todo 测试
 *
 * @author: chenyijiang
 * @date: 2021/11/3 20:48
 */
public class CapaTimeSampler implements Sampler {

    private Config config;

    public CapaTimeSampler(double sampleRatio, long interval, TimeUnit intervalTimeUnit, long minSampleCount,
                           long maxSampleCount) {
        update(sampleRatio, interval, intervalTimeUnit, minSampleCount, maxSampleCount);
    }

    public void update(double sampleRatio, long interval, TimeUnit intervalTimeUnit, long minSampleCount,
                       long maxSampleCount) {
        config = new Config(sampleRatio, interval, intervalTimeUnit, minSampleCount, maxSampleCount);
    }

    @Override
    public SamplingResult shouldSample(Context context, String traceId, String name, SpanKind kind,
                                       Attributes attributes, List<LinkData> list) {
        Config currentConfig = config;
        long nano = System.nanoTime();
        if (currentConfig.sampleTimeBeginNano + currentConfig.intervalNano < nano) {
            currentConfig.currentSampled.set(0L);
            currentConfig.sampleTimeBeginNano = nano;
        }

        if (currentConfig.currentSampled.incrementAndGet() <= currentConfig.minSampleCount) {
            return SamplingResult.recordAndSample();
        } else {
            currentConfig.currentSampled.decrementAndGet();
        }

        if (currentConfig.inner.shouldSample(context, traceId, name, kind, attributes, list) == SamplingResult
                .recordAndSample()
                && currentConfig.currentSampled.incrementAndGet() <= currentConfig.maxSampleCount) {
            return SamplingResult.recordAndSample();
        } else {
            currentConfig.currentSampled.decrementAndGet();
        }

        return SamplingResult.drop();
    }

    @Override
    public String getDescription() {
        return "";
    }

    static class Config {

        // by time
        final long intervalNano;

        final long minSampleCount;

        final long maxSampleCount;

        final AtomicLong currentSampled;

        final Sampler inner;

        long sampleTimeBeginNano = System.nanoTime();

        public Config(double sampleRatio, long interval, TimeUnit intervalTimeUnit, long minSampleCount,
                      long maxSampleCount) {
            inner = Sampler.traceIdRatioBased(sampleRatio);
            this.intervalNano = intervalTimeUnit.toNanos(interval);
            this.minSampleCount = minSampleCount;
            this.maxSampleCount = maxSampleCount;
            this.currentSampled = new AtomicLong(0L);
        }


    }
}
