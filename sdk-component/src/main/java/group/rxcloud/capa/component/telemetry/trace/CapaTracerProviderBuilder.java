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
import group.rxcloud.capa.infrastructure.loader.CapaClassLoader;
import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
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
import java.util.List;

/**
 * Builder for capa tracer provider.
 */
@NotThreadSafe
public class CapaTracerProviderBuilder {

    /**
     * Build the tracer provider with the config.
     * In the following cases, a noop implementation will be returned.
     * 1. No processor config was defined.
     *
     * @return the meter provider.
     */
    public CapaTracerProvider buildTracerProvider() {
        TracerConfigLoader tracerConfigLoader = CapaClassLoader.loadComponentClassObj("telemetry-common", TracerConfigLoader.class);

        if (tracerConfigLoader.getProcessors() == null || tracerConfigLoader.getProcessors().isEmpty()) {
            return new CapaTracerProvider(TracerProvider.noop());
        }

        SdkTracerProviderBuilder builder = SdkTracerProvider.builder();
        this.addSpanLimits(builder, tracerConfigLoader);
        this.addIdGenerator(builder, tracerConfigLoader);
        this.addSampler(builder, tracerConfigLoader);
        this.addProcessors(builder, tracerConfigLoader);

        SdkTracerProvider provider = builder.build();

        if (tracerConfigLoader != null && !tracerConfigLoader.isEnableIdValidate()) {
            InnerModule.skipIdValidate(provider);
        }

        return new CapaTracerProvider(provider);
    }

    private void addSpanLimits(SdkTracerProviderBuilder builder, TracerConfigLoader tracerConfigLoader) {
        SpanLimitsBuilder limits = SpanLimits.builder();

        SpanLimitsConfig spanLimits = tracerConfigLoader.getSpanLimits();
        if (spanLimits != null) {
            boolean added = addSpanLimits(spanLimits, limits);
            if (added) {
                builder.setSpanLimits(limits.build());
            }
        }
    }

    private void addIdGenerator(SdkTracerProviderBuilder builder, TracerConfigLoader tracerConfigLoader) {
        IdGenerator idGenerator = tracerConfigLoader.getIdGenerator();
        if (idGenerator != null) {
            builder.setIdGenerator(idGenerator);
        }
    }

    private void addSampler(SdkTracerProviderBuilder builder, TracerConfigLoader tracerConfigLoader) {
        SamplerConfig samplerConfig = tracerConfigLoader.getSamplerConfig();
        CapaTraceSampler root = new CapaTraceSampler(samplerConfig);
        Sampler sampler = Sampler.parentBased(root);
        builder.setSampler(sampler);
    }

    private void addProcessors(SdkTracerProviderBuilder builder, TracerConfigLoader tracerConfigLoader) {
        if (tracerConfigLoader != null) {
            List<SpanProcessor> processors = tracerConfigLoader.getProcessors();
            if (processors != null) {
                processors.forEach(processor -> {
                    if (processor != null) {
                        builder.addSpanProcessor(processor);
                    }
                });
            }
        }
    }

    private boolean addSpanLimits(SpanLimitsConfig spanLimits, SpanLimitsBuilder limits) {
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
}

interface InnerModule {

    static void skipIdValidate(SdkTracerProvider provider) {
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
}