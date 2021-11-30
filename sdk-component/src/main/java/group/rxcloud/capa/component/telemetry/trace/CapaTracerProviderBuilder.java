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

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.utils.SpiUtils;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.SpanLimits;
import io.opentelemetry.sdk.trace.SpanLimitsBuilder;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Builder for capa tracer provider.
 */
@NotThreadSafe
public class CapaTracerProviderBuilder implements CapaTracerProviderSettings {

    /**
     * Config for capa tracer provider.
     */
    private TracerConfig tracerConfig;

    /**
     * Id generator instance.
     */
    private IdGenerator idGenerator;

    /**
     * Span processor instances.
     */
    private List<SpanProcessor> processors;

    /**
     * Span limits.
     */
    private SpanLimitsConfig spanLimitsConfig;

    /**
     * Sampler config.
     */
    private Supplier<SamplerConfig> samplerConfig = SamplerConfig.DEFAULT_SUPPLIER;

    private static boolean addSpanLimits(SpanLimitsConfig spanLimits, SpanLimitsBuilder limits) {
        if (spanLimits == null) {
            return false;
        }
        boolean added = false;
        if (spanLimits.getMaxAttributeValueLength() != null) {
            limits.setMaxAttributeValueLength(spanLimits.getMaxAttributeValueLength());
            added = true;
        }
        if (spanLimits.getMaxNumAttributes() != null) {
            limits.setMaxNumberOfAttributes(spanLimits.getMaxNumAttributes());
            added = true;
        }
        if (spanLimits.getMaxNumEvents() != null) {
            limits.setMaxNumberOfEvents(spanLimits.getMaxNumEvents());
            added = true;
        }
        if (spanLimits.getMaxNumLinks() != null) {
            limits.setMaxNumberOfLinks(spanLimits.getMaxNumLinks());
            added = true;
        }
        if (spanLimits.getMaxNumAttributesPerLink() != null) {
            limits.setMaxNumberOfAttributesPerLink(spanLimits.getMaxNumAttributesPerLink());
            added = true;
        }
        if (spanLimits.getMaxNumAttributesPerEvent() != null) {
            limits.setMaxNumberOfAttributesPerEvent(spanLimits.getMaxNumAttributesPerEvent());
            added = true;
        }
        return added;
    }

    private static void skipIdValidate(SdkTracerProvider provider) {
        try {
            Field fieldTracerSharedState = SdkTracerProvider.class.getDeclaredField("sharedState");
            fieldTracerSharedState.setAccessible(true);
            Object sharedStatus = fieldTracerSharedState.get(provider);
            Class sharedStatusType = sharedStatus.getClass();
            Field fieldFlag = sharedStatusType.getDeclaredField("idGeneratorSafeToSkipIdValidation");
            fieldFlag.setAccessible(true);
            fieldFlag.set(sharedStatus, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CapaException(CapaErrorContext.SYSTEM_ERROR, "Fail to skip id validation.", e);
        }
    }

    @Override
    public CapaTracerProviderBuilder setSamplerConfig(Supplier<SamplerConfig> samplerConfig) {
        this.samplerConfig = samplerConfig;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder setTracerConfig(TracerConfig tracerConfig) {
        this.tracerConfig = tracerConfig;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder setSpanLimits(SpanLimitsConfig spanLimits) {
        spanLimitsConfig = spanLimits;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder addProcessor(SpanProcessor processor) {
        if (processors == null) {
            processors = new ArrayList<>();
        }
        processors.add(processor);
        return this;
    }

    private void initTracerConfig() {
        if (tracerConfig == null) {
            tracerConfig = SpiUtils.loadConfigNullable(CapaTracerProviderSettings.FILE_PATH, TracerConfig.class);
        }
    }

    /**
     * Build the tracer provider with the config.
     * In the following cases, a noop implementation will be returned.
     * 1. No processor config was defined.
     *
     * @return the meter provider.
     */
    public CapaTracerProvider buildTracerProvider() {
        // if config was not explicitly set, try loading the config from the config loader.
        initTracerConfig();

        if ((processors == null || processors.isEmpty()) && (tracerConfig == null
                || tracerConfig.getProcessors() == null
                || tracerConfig.getProcessors().isEmpty())) {
            return new CapaTracerProvider(TracerProvider.noop());
        }

        SdkTracerProviderBuilder builder = SdkTracerProvider.builder();
        addSpanLimits(builder);
        addIdGenerator(builder);
        addSampler(builder);
        addProcessors(builder);

        SdkTracerProvider provider = builder.build();

        if (tracerConfig != null && !tracerConfig.isEnableIdValidate()) {
            skipIdValidate(provider);
        }

        return new CapaTracerProvider(provider);
    }

    private void addIdGenerator(SdkTracerProviderBuilder builder) {
        if (idGenerator != null) {
            builder.setIdGenerator(idGenerator);
        } else if (tracerConfig != null) {
            IdGenerator generator = SpiUtils
                    .newInstanceWithConstructorCache(tracerConfig.getIdGenerator(), IdGenerator.class);
            if (generator != null) {
                builder.setIdGenerator(generator);
            }
        }
    }

    private void addSpanLimits(SdkTracerProviderBuilder builder) {
        SpanLimitsBuilder limits = SpanLimits.builder();
        boolean added = false;
        if (tracerConfig != null && tracerConfig.getSpanLimits() != null) {
            added |= addSpanLimits(tracerConfig.getSpanLimits(), limits);
        }
        if (spanLimitsConfig != null) {
            added |= addSpanLimits(spanLimitsConfig, limits);
        }

        if (added) {
            builder.setSpanLimits(limits.build());
        }
    }

    private void addSampler(SdkTracerProviderBuilder builder) {
        builder.setSampler(Sampler.parentBased(new CapaTraceSampler(samplerConfig)));
    }

    private void addProcessors(SdkTracerProviderBuilder builder) {
        List<SpanProcessor> processors = this.processors;
        if (processors != null && !processors.isEmpty()) {
            processors.forEach(p -> builder.addSpanProcessor(p));
        } else if (tracerConfig != null && tracerConfig.getProcessors() != null) {
            tracerConfig.getProcessors().forEach(p -> {
                SpanProcessor processor = SpiUtils.newInstanceWithConstructorCache(p, SpanProcessor.class);
                if (processor != null) {
                    builder.addSpanProcessor(processor);
                }
            });
        }
    }
}

