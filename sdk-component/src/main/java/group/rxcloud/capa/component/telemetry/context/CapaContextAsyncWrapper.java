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

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Method for async context.
 */
public final class CapaContextAsyncWrapper {

    private static final CapaContextAsyncWrapperPlugin WRAPPER_PLUGIN = CapaContextAsyncWrapperPlugin.loadPlugin();

    private CapaContextAsyncWrapper() {
    }

    /**
     * Task wrapping runnable.
     *
     * @param runnable the runnable
     * @return the runnable
     */
    public static Runnable taskWrapping(Runnable runnable) {
        return WRAPPER_PLUGIN.wrap(runnable);
    }

    /**
     * Task wrapping callable.
     *
     * @param <T>      the type parameter
     * @param callable the callable
     * @return the callable
     */
    public static <T> Callable<T> taskWrapping(Callable<T> callable) {
        return WRAPPER_PLUGIN.wrap(callable);
    }

    /**
     * Task wrapping executor.
     *
     * @param executor the executor
     * @return the executor
     */
    public static Executor taskWrapping(Executor executor) {
        return WRAPPER_PLUGIN.wrap(executor);
    }

    /**
     * Task wrapping executor service.
     *
     * @param executor the executor
     * @return the executor service
     */
    public static ExecutorService taskWrapping(ExecutorService executor) {
        return WRAPPER_PLUGIN.wrap(executor);
    }

    /**
     * Task wrapping scheduled executor service.
     *
     * @param executor the executor
     * @return the scheduled executor service
     */
    public static ScheduledExecutorService taskWrapping(ScheduledExecutorService executor) {
        return WRAPPER_PLUGIN.wrap(executor);
    }

    /**
     * Gets trace id.
     *
     * @return the trace id
     */
    public static String getTraceId() {
        return WRAPPER_PLUGIN.getTraceId();
    }
}
