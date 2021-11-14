package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.trace.config.SamplerConfig;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capa Sampler Cache.
 *
 * @author: chenyijiang
 * @date: 2021/11/3 17:48
 */
public final class CapaSamplerProvider {

    private static final Map<String, CapaSampler> CACHE = new ConcurrentHashMap<>();

    @Nonnull
    public static Sampler getDefault() {
        return getOrCreate(SamplerConfig.DEFAULT_CONFIG);
    }

    @Nonnull
    public static Sampler updateOrCreate(@Nonnull SamplerConfig samplerConfig) {
        if (CACHE.containsKey(samplerConfig.getName())) {
            CapaSampler sampler = CACHE.get(samplerConfig.getName());
            sampler.update(samplerConfig);
            return Sampler.parentBased(sampler);
        } else {
            return getOrCreate(samplerConfig);
        }
    }

    @Nonnull
    public static Sampler getOrCreate(@Nullable SamplerConfig samplerConfig) {
        if (samplerConfig == null) {
            return getDefault();
        }

        Sampler sampler = CACHE.computeIfAbsent(samplerConfig.getName(), k -> {
            return new CapaSampler(samplerConfig);
        });
        return Sampler.parentBased(sampler);
    }

    @Nullable
    public Sampler get(String name) {
        return CACHE.get(name);
    }

}
