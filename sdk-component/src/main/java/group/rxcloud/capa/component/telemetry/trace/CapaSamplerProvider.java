/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.trace.config.SamplerConfig;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capa Sampler Cache.
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
