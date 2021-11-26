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
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:07
 */
public class TestPropagator implements TextMapPropagator {

    private static AtomicInteger called = new AtomicInteger();

    @Override
    public Collection<String> fields() {
        return Lists.newArrayList("a");
    }

    @Override
    public <C> void inject(Context context, @Nullable C carrier, TextMapSetter<C> setter) {
        called.incrementAndGet();
    }

    @Override
    public <C> Context extract(Context context, @Nullable C carrier, TextMapGetter<C> getter) {
        return null;
    }

    public static int getCalled() {
        return called.get();
    }

    public static void clean() {
        called.set(0);
    }
}
