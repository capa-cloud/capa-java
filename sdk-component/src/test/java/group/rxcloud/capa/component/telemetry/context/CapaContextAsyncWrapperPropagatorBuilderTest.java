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

import com.google.common.collect.Lists;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:03
 */
public class CapaContextAsyncWrapperPropagatorBuilderTest {

    @Test
    public void setContextConfig() {
        TestPropagator.clean();
        ContextConfig config = new ContextConfig();
        config.setContextPropagators(
                Lists.newArrayList("group.rxcloud.capa.component.telemetry.context.TestPropagator"));
        ContextPropagators propagators = new CapaContextPropagatorBuilder().setContextConfig(config)
                                                                           .buildContextPropagators();
        propagators.getTextMapPropagator().inject(new Context() {
            @Nullable
            @Override
            public <V> V get(ContextKey<V> key) {
                return null;
            }

            @Override
            public <V> Context with(ContextKey<V> k1, V v1) {
                return null;
            }
        }, null, new TextMapSetter<Object>() {
            @Override
            public void set(@Nullable Object carrier, String key, String value) {

            }
        });
        assertEquals(1, TestPropagator.getCalled());
    }

    @Test
    public void addContextPropagators() {
        TestPropagator.clean();
        ContextPropagators propagators = new CapaContextPropagatorBuilder().addContextPropagators(new TestPropagator())
                                                                           .addContextPropagators(new TestPropagator())
                                                                           .buildContextPropagators();

        propagators.getTextMapPropagator().inject(new Context() {
            @Nullable
            @Override
            public <V> V get(ContextKey<V> key) {
                return null;
            }

            @Override
            public <V> Context with(ContextKey<V> k1, V v1) {
                return null;
            }
        }, null, new TextMapSetter<Object>() {
            @Override
            public void set(@Nullable Object carrier, String key, String value) {

            }
        });

        assertEquals(5, TestPropagator.getCalled());
    }


    @Test
    public void buildFromContextConfig() {
        TestPropagator.clean();
        ContextPropagators propagators = new CapaContextPropagatorBuilder()
                .buildContextPropagators();
        propagators.getTextMapPropagator().inject(new Context() {
            @Nullable
            @Override
            public <V> V get(ContextKey<V> key) {
                return null;
            }

            @Override
            public <V> Context with(ContextKey<V> k1, V v1) {
                return null;
            }
        }, null, new TextMapSetter<Object>() {
            @Override
            public void set(@Nullable Object carrier, String key, String value) {

            }
        });
        assertEquals(3, TestPropagator.getCalled());
    }

    @Test
    public void buildFromLoader() {
        TestPropagator.clean();
        ContextPropagators propagators = new CapaContextPropagatorBuilder()
                .setContextConfig(new ContextConfig())
                .buildContextPropagators();
        propagators.getTextMapPropagator().inject(new Context() {
            @Nullable
            @Override
            public <V> V get(ContextKey<V> key) {
                return null;
            }

            @Override
            public <V> Context with(ContextKey<V> k1, V v1) {
                return null;
            }
        }, null, new TextMapSetter<Object>() {
            @Override
            public void set(@Nullable Object carrier, String key, String value) {

            }
        });
        assertNotNull(propagators);
        assertEquals(0, TestPropagator.getCalled());
    }

}