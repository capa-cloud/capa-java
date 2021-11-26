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

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 20:53
 */
public class CapaContextTest {

    @Test
    public void taskWrapping() {
        CapaContext.taskWrapping(new Runnable() {
            @Override
            public void run() {

            }
        });
        assertTrue(TestCapaContextAsyncWrapper.called(Runnable.class));
        CapaContext.taskWrapping(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
        assertTrue(TestCapaContextAsyncWrapper.called(Callable.class));
        CapaContext.taskWrapping(Executors.newSingleThreadExecutor());
        assertTrue(TestCapaContextAsyncWrapper.called(ExecutorService.class));
        CapaContext.taskWrapping(new Executor() {
            @Override
            public void execute(@NotNull Runnable command) {

            }
        });
        assertTrue(TestCapaContextAsyncWrapper.called(Executor.class));
        CapaContext.taskWrapping(Executors.newScheduledThreadPool(1));
        assertTrue(TestCapaContextAsyncWrapper.called(ScheduledExecutorService.class));

        assertNotNull(CapaContext.getTraceId());
    }
}