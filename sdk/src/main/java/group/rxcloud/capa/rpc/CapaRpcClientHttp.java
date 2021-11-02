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

import group.rxcloud.capa.component.http.CapaHttp;
import group.rxcloud.capa.infrastructure.exceptions.CapaExceptions;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An adapter for the HTTP RPC Client.
 */
public class CapaRpcClientHttp extends AbstractCapaRpcClient {

    /**
     * The HTTP client to be used.
     *
     * @see CapaHttp
     */
    private final CapaHttp client;

    public CapaRpcClientHttp(CapaHttp client) {
        this.client = client;
    }

    @Override
    public <T> Mono<T> invokeMethod(InvokeMethodRequest invokeMethodRequest, TypeRef<T> type) {
        try {
            final String appId = invokeMethodRequest.getAppId();
            final String method = invokeMethodRequest.getMethod();
            final Object request = invokeMethodRequest.getBody();
            final HttpExtension httpExtension = invokeMethodRequest.getHttpExtension();
            final Map<String, String> metadata = invokeMethodRequest.getMetadata();
            // check appId
            if (appId == null || appId.trim().isEmpty()) {
                throw new IllegalArgumentException("App Id cannot be null or empty.");
            }
            // check method
            if (method == null || method.trim().isEmpty()) {
                throw new IllegalArgumentException("Method name cannot be null or empty.");
            }
            // check httpExtension
            if (httpExtension == null) {
                throw new IllegalArgumentException("HttpExtension cannot be null. Use HttpExtension.NONE instead.");
            }
            // If the httpExtension is not null, then the method will not be null based on checks in constructor
            final String httpMethod = httpExtension.getMethod().toString();
            final Map<String, String> httpExtensionHeaders = httpExtension.getHeaders();
            final Map<String, List<String>> urlParameters = httpExtension.getQueryParams();

            String[] pathSegments = new String[]{
                    CapaHttp.API_VERSION,
                    "invoke", appId,
                    "method", method};

            Map<String, String> headers = new HashMap<>(httpExtensionHeaders);
            if (metadata != null && !metadata.isEmpty()) {
                headers.putAll(metadata);
            }

            return Mono.subscriberContext()
                    .flatMap(context ->
                            this.client.invokeApi(
                                    httpMethod,
                                    pathSegments,
                                    urlParameters,
                                    request,
                                    headers,
                                    context,
                                    type))
                    .flatMap(httpResponse -> {
                        final T responseBody = httpResponse.getBody();
                        if (responseBody != null) {
                            return Mono.just(responseBody);
                        }
                        return Mono.empty();
                    });
        } catch (Exception ex) {
            return CapaExceptions.wrapMono(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            throw CapaExceptions.propagate(e);
        }
    }
}
