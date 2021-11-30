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
package group.rxcloud.capa.spi.demo.http;

import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.spi.demo.http.config.DemoRpcServiceOptions;
import group.rxcloud.capa.spi.http.CapaSerializeHttpSpi;
import group.rxcloud.capa.spi.http.config.RpcServiceOptions;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The Demo capa http invoker.
 */
public class DemoCapaHttp extends CapaSerializeHttpSpi {

    private static final Logger logger = LoggerFactory.getLogger(DemoCapaHttp.class);

    /**
     * Instantiates a new Capa serialize http spi.
     *
     * @param httpClient       the http client
     * @param objectSerializer the object serializer
     */
    public DemoCapaHttp(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
        super(httpClient, objectSerializer);
    }

    @Override
    protected <T> CompletableFuture<HttpResponse<T>> invokeSpiApi(String appId,
                                                                  String method,
                                                                  Object requestData,
                                                                  Map<String, String> headers,
                                                                  TypeRef<T> type,
                                                                  RpcServiceOptions rpcServiceOptions) {
        DemoRpcServiceOptions demoRpcServiceOptions = (DemoRpcServiceOptions) rpcServiceOptions;
        logger.info("[DemoCapaHttp.invokeSpiApi] rpcServiceOptions[{}]", demoRpcServiceOptions);

        return CompletableFuture.supplyAsync(
                () -> {
                    logger.info("[DemoCapaHttp.invokeSpiApi] supplyAsync[{}]", System.currentTimeMillis());
                    return new HttpResponse<>(null, null, 200);
                },
                Runnable::run);
    }
}
