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

import group.rxcloud.capa.component.telemetry.SpiUtils;
import group.rxcloud.capa.component.telemetry.trace.config.SpanLimitsConfig;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.SpanLimits;
import io.opentelemetry.sdk.trace.SpanLimitsBuilder;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class CapaTracerProviderBuilder implements CapaTracerProviderSettings, CapaContextPropagatorSettings {

    private TracerConfig tracerConfig;

    private IdGenerator idGenerator;

    private Sampler sampler;

    private List<SpanProcessor> processors;

    private SpanLimitsConfig spanLimitsConfig;

    private List<TextMapPropagator> contextPropagators;

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

    @Override
    public CapaTracerProviderBuilder setTracerConfig(TracerConfig tracerConfig) {
        this.tracerConfig = tracerConfig;
        return this;
    }

    @Override
    public CapaTracerProviderSettings setSpanLimits(SpanLimitsConfig spanLimits) {
        this.spanLimitsConfig = spanLimits;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder setSampler(Sampler sampler) {
        this.sampler = sampler;
        return this;
    }

    @Override
    public CapaTracerProviderBuilder setProcessors(List<SpanProcessor> processors) {
        if (processors != null) {
            this.processors = new ArrayList<>(processors);
        }
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

    @Override
    public CapaContextPropagatorSettings setContextPropagators(List<TextMapPropagator> contextPropagators) {
        if (contextPropagators != null) {
            this.contextPropagators = new ArrayList<>(contextPropagators);
        }
        return this;
    }

    @Override
    public CapaContextPropagatorSettings addContextPropagators(TextMapPropagator processor) {
        if (contextPropagators != null) {
            contextPropagators = new ArrayList<>();
        }
        contextPropagators.add(processor);
        return this;
    }

    private void initTracerConfig() {
        if (tracerConfig == null) {
            CapaTracerConfigLoader tracerConfigLoader = SpiUtils.getFromSpiConfigFile(CapaTracerConfigLoader.class);
            if (tracerConfigLoader == null) {
                tracerConfigLoader = CapaTracerConfigLoader.DEFAULT;
            }
            tracerConfig = tracerConfigLoader.loadTracerConfig();
        }
    }

    public CapaTracerProvider buildTracerProvider() {
        // if config was not explicitly set, try loading the config from the config loader.
        initTracerConfig();

        SdkTracerProviderBuilder builder = SdkTracerProvider.builder();
        addSpanLimits(builder);
        addIdGenerator(builder);
        addSampler(builder);
        addProcessors(builder);

        SdkTracerProvider provider = builder.build();

        if (!tracerConfig.isEnableIdValidate()) {
            skipIdValidate(provider);
        }

        return new CapaTracerProvider(provider);
    }

    public ContextPropagators buildContextPropagators() {
        List<TextMapPropagator> propagators = contextPropagators;

        if (propagators == null) {
            initTracerConfig();
            if (tracerConfig != null) {
                propagators = new ArrayList<>();
                if (tracerConfig.getContextPropagatorsInstance() != null && !tracerConfig
                        .getContextPropagatorsInstance().isEmpty()) {
                    propagators.addAll(tracerConfig.getContextPropagatorsInstance());
                }
                if (tracerConfig.getContextPropagators() != null && !tracerConfig
                        .getContextPropagators().isEmpty()) {
                    propagators.addAll(tracerConfig.getContextPropagators().stream()
                                                   .map(path -> SpiUtils
                                                           .getFromSpiConfigFile(path, TextMapPropagator.class))
                                                   .collect(Collectors.toList()));
                }

            }
        }

        return propagators == null || propagators.isEmpty() ? ContextPropagators.noop() : ContextPropagators
                .create(TextMapPropagator.composite(propagators.toArray(new TextMapPropagator[0])));
    }

    private void addIdGenerator(SdkTracerProviderBuilder builder) {
        if (idGenerator != null) {
            builder.setIdGenerator(idGenerator);
        } else if (tracerConfig != null) {
            IdGenerator generator = SpiUtils.getFromSpiConfigFile(tracerConfig.getIdGenerator(), IdGenerator.class);
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
        } else if (spanLimitsConfig != null) {
            added |= addSpanLimits(spanLimitsConfig, limits);
        }
        if (added) {
            builder.setSpanLimits(limits.build());
        }
    }

    private void addSampler(SdkTracerProviderBuilder builder) {
        if (sampler != null) {
            builder.setSampler(sampler);
        } else if (tracerConfig != null) {
            builder.setSampler(CapaSamplerProvider.getOrCreate(tracerConfig.getSampler()));
        }
    }

    private void addProcessors(SdkTracerProviderBuilder builder) {
        if (processors != null && !processors.isEmpty()) {
            processors.forEach(p -> builder.addSpanProcessor(p));
        } else if (tracerConfig != null) {
            if (tracerConfig.getProcessorsInstance() != null) {
                tracerConfig.getProcessorsInstance().forEach(p -> builder.addSpanProcessor(p));
            }
            if (tracerConfig.getProcessors() != null) {
                tracerConfig.getProcessors().forEach(p -> {
                    SpanProcessor processor = SpiUtils.getFromSpiConfigFile(p, SpanProcessor.class);
                    if (processor != null) {
                        builder.addSpanProcessor(processor);
                    }
                });
            }
        }
    }

    private void skipIdValidate(SdkTracerProvider provider) {
        try {
            Field fieldTracerSharedState = SdkTracerProvider.class.getDeclaredField("sharedState");
            fieldTracerSharedState.setAccessible(true);
            Object sharedStatus = fieldTracerSharedState.get(provider);
            Class sharedStatusType = sharedStatus.getClass();
            Field fieldFlag = sharedStatusType.getDeclaredField("idGeneratorSafeToSkipIdValidation");
            fieldFlag.setAccessible(true);
            fieldFlag.set(sharedStatus, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // todo
            throw new CapaException(e);
        }
    }

}
