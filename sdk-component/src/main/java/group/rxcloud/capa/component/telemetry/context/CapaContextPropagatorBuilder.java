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
package group.rxcloud.capa.component.telemetry.context;

import group.rxcloud.capa.infrastructure.utils.SpiUtils;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for capa context propagator.
 */
@NotThreadSafe
public class CapaContextPropagatorBuilder implements CapaContextPropagatorSettings {

    /**
     * Context config.
     */
    private ContextConfig contextConfig;

    /**
     * Context propagator instances.
     */
    private List<TextMapPropagator> contextPropagatorsInstance;

    @Override
    public CapaContextPropagatorBuilder setContextConfig(ContextConfig config) {
        contextConfig = config;
        return this;
    }

    @Override
    public CapaContextPropagatorBuilder addContextPropagators(TextMapPropagator processor) {
        if (contextPropagatorsInstance == null) {
            contextPropagatorsInstance = new ArrayList<>();
        }
        contextPropagatorsInstance.add(processor);
        return this;
    }

    /**
     * Build context propagators.
     *
     * @return context propagators.
     */
    public ContextPropagators buildContextPropagators() {
        if (contextPropagatorsInstance != null && !contextPropagatorsInstance.isEmpty()) {
            return ContextPropagators
                    .create(TextMapPropagator.composite(contextPropagatorsInstance.toArray(new TextMapPropagator[0])));
        }

        initContextConfig();
        if (contextConfig != null) {
            List<String> types = contextConfig.getContextPropagators();
            if (types != null && !types.isEmpty()) {
                return ContextPropagators
                        .create(TextMapPropagator.composite(types.stream()
                                                                 .map(path -> SpiUtils
                                                                         .newInstanceWithConstructorCache(path, TextMapPropagator.class))
                                                                 .toArray(TextMapPropagator[]::new)));
            }
        }

        ContextPropagatorLoader loader = SpiUtils.loadFromSpiComponentFileNullable(ContextPropagatorLoader.class, "telemetry");
        if (loader == null) {
            loader = ContextPropagatorLoader.DEFAULT;
        }
        return loader.load();
    }

    private void initContextConfig() {
        if (contextConfig == null) {
            contextConfig = SpiUtils.loadConfigNullable(FILE_PATH, ContextConfig.class);;
        }
    }
}
