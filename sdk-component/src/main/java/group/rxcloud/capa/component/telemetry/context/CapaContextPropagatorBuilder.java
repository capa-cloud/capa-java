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

import group.rxcloud.capa.infrastructure.loader.CapaClassLoader;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for capa context propagator.
 */
@NotThreadSafe
public class CapaContextPropagatorBuilder {

    /**
     * Build context propagators.
     *
     * @return context propagators.
     */
    public ContextPropagators buildContextPropagators() {
        List<TextMapPropagator> contextPropagatorsInstance = new ArrayList<>();

        ContextPropagatorLoader loader = CapaClassLoader.loadComponentClassObj("telemetry-common", ContextPropagatorLoader.class);
        List<TextMapPropagator> loaded = loader.load();
        if (loaded != null) {
            contextPropagatorsInstance.addAll(loaded);
        }

        if (contextPropagatorsInstance.isEmpty()) {
            return ContextPropagators.noop();
        }

        TextMapPropagator[] textMapPropagators = contextPropagatorsInstance.toArray(new TextMapPropagator[0]);
        TextMapPropagator textPropagator = TextMapPropagator.composite(textMapPropagators);
        return ContextPropagators.create(textPropagator);
    }
}
