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
package group.rxcloud.capa.rpc;

import group.rxcloud.capa.AbstractCapaClient;
import group.rxcloud.capa.rpc.domain.InvokeMethodRequestBuilder;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Abstract class with convenient methods common between client implementations.
 *
 * @see CapaRpcClientHttp
 */
public abstract class AbstractCapaRpcClient
        extends AbstractCapaClient
        implements CapaRpcClient {

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata, TypeRef<T> type) {
        InvokeMethodRequestBuilder builder = new InvokeMethodRequestBuilder(appId, methodName);
        InvokeMethodRequest req = builder
                .withBody(request)
                .withHttpExtension(httpExtension)
                .withMetadata(metadata)
                .build();
        return this.invokeMethod(req, type);
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata, Class<T> clazz) {
        return this.invokeMethod(appId, methodName, request, httpExtension, metadata, TypeRef.get(clazz));
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, TypeRef<T> type) {
        return this.invokeMethod(appId, methodName, request, httpExtension, null, type);
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Class<T> clazz) {
        return this.invokeMethod(appId, methodName, request, httpExtension, null, TypeRef.get(clazz));
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata, TypeRef<T> type) {
        return this.invokeMethod(appId, methodName, null, httpExtension, metadata, type);
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata, Class<T> clazz) {
        return this.invokeMethod(appId, methodName, null, httpExtension, metadata, TypeRef.get(clazz));
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata) {
        return this.invokeMethod(appId, methodName, request, httpExtension, metadata, TypeRef.VOID);
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension) {
        return this.invokeMethod(appId, methodName, request, httpExtension, null, TypeRef.VOID);
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata) {
        return this.invokeMethod(appId, methodName, null, httpExtension, metadata, TypeRef.VOID);
    }

    @Override
    public Mono<byte[]> invokeMethod(String appId, String methodName, byte[] request, HttpExtension httpExtension, Map<String, String> metadata) {
        return this.invokeMethod(appId, methodName, request, httpExtension, metadata, TypeRef.BYTE_ARRAY);
    }
}
