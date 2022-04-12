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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 20:55
 */
public class TestCapaContextAsyncWrapperPlugin extends CapaContextAsyncWrapperPlugin.CapaContextAsyncWrapperDefault {

    private static Set<Class> called = new HashSet<>();

    @Override
    public Runnable wrap(Runnable runnable) {
        called.add(Runnable.class);
        return super.wrap(runnable);
    }

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        called.add(Callable.class);
        return super.wrap(callable);
    }

    @Override
    public Executor wrap(Executor executor) {
        called.add(Executor.class);
        return super.wrap(executor);
    }

    @Override
    public ExecutorService wrap(ExecutorService executor) {
        called.add(ExecutorService.class);
        return super.wrap(executor);
    }

    @Override
    public ScheduledExecutorService wrap(ScheduledExecutorService executor) {
        called.add(ScheduledExecutorService.class);
        return super.wrap(executor);
    }

    @Override
    public String getTraceId() {
        return super.getTraceId();
    }

    public static boolean called(Class type) {
        return called.contains(type);
    }
}
