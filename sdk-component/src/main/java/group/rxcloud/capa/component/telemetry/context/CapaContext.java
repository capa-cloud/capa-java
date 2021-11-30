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

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Method for async context.
 */
public final class CapaContext {

    private static final CapaContextAsyncWrapper WRAPPER = getAsyncWrapper();

    private CapaContext() {
    }

    public static Runnable taskWrapping(Runnable runnable) {
        return WRAPPER.wrap(runnable);
    }

    public static <T> Callable<T> taskWrapping(Callable<T> callable) {
        return WRAPPER.wrap(callable);
    }

    public static Executor taskWrapping(Executor executor) {
        return WRAPPER.wrap(executor);
    }

    public static ExecutorService taskWrapping(ExecutorService executor) {
        return WRAPPER.wrap(executor);
    }

    public static ScheduledExecutorService taskWrapping(ScheduledExecutorService executor) {
        return WRAPPER.wrap(executor);
    }

    public static String getTraceId() {
        return WRAPPER.getTraceId();
    }

    private static CapaContextAsyncWrapper getAsyncWrapper() {
        CapaContextAsyncWrapper WRAPPER = SpiUtils.loadFromSpiComponentFileNullable(CapaContextAsyncWrapper.class, "telemetry");
        if (WRAPPER == null) {
            return new CapaContextAsyncWrapper() {};
        }
        return WRAPPER;
    }
}
