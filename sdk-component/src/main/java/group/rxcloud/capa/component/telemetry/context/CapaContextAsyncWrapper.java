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

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 */
public interface CapaContextAsyncWrapper {

    default Runnable wrap(Runnable runnable) {
        return Context.current().wrap(runnable);
    }

    default <T> Callable<T> wrap(Callable<T> callable) {
        return Context.current().wrap(callable);
    }

    default Executor wrap(Executor executor) {
        return Context.current().wrap(executor);
    }

    default ExecutorService wrap(ExecutorService executor) {
        return Context.current().wrap(executor);
    }

    default ScheduledExecutorService wrap(ScheduledExecutorService executor) {
        return Context.current().wrap(executor);
    }

    default String getTraceId() {
        return Span.fromContext(Context.current()).getSpanContext().getTraceId();
    }
}
