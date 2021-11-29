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
package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.TelemetryHooks;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import reactor.util.context.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The capa http invoker used in tests only.
 */
public class TestCapaHttp extends CapaHttp {

    public TestCapaHttp(OkHttpClient httpClient, CapaObjectSerializer objectSerializer,
                        TelemetryHooks telemetryHooks, ConfigurationHooks configurationHooks) {
        super(httpClient, objectSerializer, telemetryHooks, configurationHooks);
    }

    @Override
    protected <T> CompletableFuture<HttpResponse<T>> doInvokeApi(String httpMethod,
                                                                 String[] pathSegments,
                                                                 Map<String, List<String>> urlParameters,
                                                                 Object requestData,
                                                                 Map<String, String> headers,
                                                                 Context context,
                                                                 TypeRef<T> type) {
        return CompletableFuture.supplyAsync(
                () -> {
                    return new HttpResponse<>(null, null, 200);
                },
                Runnable::run);
    }
}
