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
package group.rxcloud.capa.spi.http;

import group.rxcloud.capa.component.http.CapaHttp;
import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.spi.config.CapaSpiOptionsLoader;
import group.rxcloud.capa.spi.config.CapaSpiProperties;
import group.rxcloud.capa.spi.config.RpcServiceOptions;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.context.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The SPI Capa http client. Templates for different implementations.
 */
public abstract class CapaHttpSpi extends CapaHttp {

    private static final Logger logger = LoggerFactory.getLogger(CapaHttpSpi.class);

    public CapaHttpSpi(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
        super(httpClient, objectSerializer);
    }

    /**
     * Templates, delegate to specific http invoker.
     *
     * @param httpMethod    Ignore, fix to POST. FIXME
     * @param urlParameters Ignore, fix to EMPTY. FIXME
     */
    @Override
    protected <T> CompletableFuture<HttpResponse<T>> doInvokeApi(String httpMethod,
                                                                 String[] pathSegments,
                                                                 Map<String, List<String>> urlParameters,
                                                                 Object requestData,
                                                                 Map<String, String> headers,
                                                                 Context context,
                                                                 TypeRef<T> type) {
        if (logger.isDebugEnabled()) {
            if (httpMethod != null) {
                logger.debug("[CapaHttpSpi] invoke rpc httpMethod[{}]", httpMethod);
            }
            if (urlParameters != null && !urlParameters.isEmpty()) {
                logger.debug("[CapaHttpSpi] invoke rpc urlParameters[{}]", urlParameters);
            }
            if (headers != null && !headers.isEmpty()) {
                logger.debug("[CapaHttpSpi] invoke rpc headers[{}}]", headers);
            }
            if (context != null) {
                logger.debug("[CapaHttpSpi] invoke rpc context[{}]", context);
            }
        }
        // FIXME Ignore, fix to POST.
        if (!HttpExtension.POST.getMethod().toString().equalsIgnoreCase(httpMethod)) {
            if (logger.isWarnEnabled()) {
                logger.warn("[CapaHttpSpi] invoke rpc httpMethod[{}] only support POST now.",
                        httpMethod);
            }
            httpMethod = HttpExtension.POST.getMethod().toString();
        }
        // FIXME Ignore, fix to EMPTY.
        if (urlParameters != null && !urlParameters.isEmpty()) {
            if (logger.isWarnEnabled()) {
                logger.warn("[CapaHttpSpi] invoke rpc urlParameters[{}] not supported now.",
                        urlParameters);
            }
            urlParameters = null;
        }

        // parse url path segments
        Objects.requireNonNull(pathSegments, "pathSegments");
        final String version = pathSegments[0];
        final String _invoke = pathSegments[1];
        final String appId = pathSegments[2];
        final String _method = pathSegments[3];
        final String method = pathSegments[4];
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaHttpSpi] invoke rpc url_path[/version={}/{}/appId={}/{}/methodName={}]",
                    version, _invoke, appId, _method, method);
        }

        // load spi service options
        RpcServiceOptions rpcServiceOptions = getRpcServiceOptions(appId);
        Objects.requireNonNull(rpcServiceOptions, "rpcServiceOptions");
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaHttpSpi] invoke rpc options[{}]",
                    rpcServiceOptions);
        }

        // spi invoke
        CompletableFuture<HttpResponse<T>> invokeSpiApi =
                invokeSpiApi(appId, method, requestData, headers, type, rpcServiceOptions);
        invokeSpiApi.whenComplete((tHttpResponse, throwable) -> {
            if (throwable != null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("[CapaHttpSpi] invoke rpc response error",
                            throwable);
                }
                return;
            }
            if (tHttpResponse == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("[CapaHttpSpi] invoke rpc response empty[{}]",
                            tHttpResponse);
                }
                return;
            }
            final int responseStatusCode = tHttpResponse.getStatusCode();
            final Map<String, String> responseHeaders = tHttpResponse.getHeaders();
            final T responseBody = tHttpResponse.getBody();
            if (logger.isDebugEnabled()) {
                logger.debug("[CapaHttpSpi] invoke rpc response code[{}] headers[{}] body[{}]",
                        responseStatusCode, responseHeaders, responseBody);
            }
        });
        return invokeSpiApi;
    }

    /**
     * Override to get the configuration of the corresponding appId.
     */
    protected RpcServiceOptions getRpcServiceOptions(String appId) {
        CapaSpiOptionsLoader capaSpiOptionsLoader = CapaSpiProperties.getSpiOptionsLoader();
        return capaSpiOptionsLoader.loadRpcServiceOptions(appId);
    }

    /**
     * Invoke spi api and then return async completable future.
     *
     * @param <T>               the response type parameter
     * @param appId             the app id
     * @param method            the invoke method
     * @param requestData       the request data
     * @param headers           the headers
     * @param type              the response type
     * @param rpcServiceOptions the rpc service options
     * @return the async completable future
     */
    protected abstract <T> CompletableFuture<HttpResponse<T>> invokeSpiApi(String appId,
                                                                           String method,
                                                                           Object requestData,
                                                                           Map<String, String> headers,
                                                                           TypeRef<T> type,
                                                                           RpcServiceOptions rpcServiceOptions);
}
