package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * todo check logic
 *
 * @author: chenyijiang
 * @date: 2021/11/3 17:48
 */
public class CapaSamplerProvider {

    private static final Map<String, CapaTimeSampler> CACHE = new ConcurrentHashMap<>();

    public static Sampler getDefault() {
        return getOrCreate(SamplerConfig.DEFAULT_CONFIG);
    }

    public static Sampler update(SamplerConfig samplerConfig) {
        if (CACHE.containsKey(samplerConfig.getName())) {
            CapaTimeSampler sampler = CACHE.get(samplerConfig.getName());
            sampler.update(samplerConfig.getSampleRatio(), samplerConfig.getIntervalMillis(),
                    TimeUnit.MILLISECONDS, samplerConfig.getMinSampleCount(),
                    samplerConfig.getMaxSampleCount());
            return Sampler.parentBased(sampler);
        } else {
            return getOrCreate(samplerConfig);
        }
    }

    public static Sampler getOrCreate(SamplerConfig samplerConfig) {
        CapaTimeSampler sampler = CACHE.computeIfAbsent(samplerConfig.getName(), k -> {
            return build(samplerConfig);
        });
        return Sampler.parentBased(sampler);
    }

    private static CapaTimeSampler build(SamplerConfig samplerConfig) {
        return new CapaTimeSampler(samplerConfig.getSampleRatio(), samplerConfig.getIntervalMillis(),
                TimeUnit.MILLISECONDS, samplerConfig.getMinSampleCount(),
                samplerConfig.getMaxSampleCount());
    }
}
