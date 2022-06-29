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
import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
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

    /**
     * Instantiates a new Capa http spi.
     *
     * @param httpClient       the http client
     * @param objectSerializer the object serializer
     */
    public CapaHttpSpi(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
        super(httpClient, objectSerializer);
    }

    /**
     * Templates, delegate to specific http invoker.
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
                logger.debug("[Capa.Rpc.Client.http] [CapaHttpSpi] invoke rpc httpMethod[{}]", httpMethod);
            }
            if (urlParameters != null && !urlParameters.isEmpty()) {
                logger.debug("[Capa.Rpc.Client.http] [CapaHttpSpi] invoke rpc urlParameters[{}]", urlParameters);
            }
            if (headers != null && !headers.isEmpty()) {
                logger.debug("[Capa.Rpc.Client.http] [CapaHttpSpi] invoke rpc headers[{}}]", headers);
            }
            if (context != null) {
                logger.debug("[Capa.Rpc.Client.http] [CapaHttpSpi] invoke rpc context[{}]", context);
            }
        }

        // parse url path segments
        Objects.requireNonNull(pathSegments, "pathSegments");
        final String version = pathSegments[0];
        final String _invoke = pathSegments[1];
        final String appId = pathSegments[2];
        final String _method = pathSegments[3];
        final String method = pathSegments[4];
        if (logger.isDebugEnabled()) {
            logger.debug("[Capa.Rpc.Client.http] [CapaHttpSpi] invoke rpc url_path[/version={}/{}/appId={}/{}/methodName={}]",
                    version, _invoke, appId, _method, method);
        }

        try {
            // spi invoke
            CompletableFuture<HttpResponse<T>> invokeSpiApi = invokeSpiApi(
                    appId, method, requestData, httpMethod, headers, urlParameters, type);
            invokeSpiApi.whenComplete(this::callbackLog);
            return invokeSpiApi;
        } catch (CapaException e) {
            throw e;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Capa.Rpc.Client.http] [CapaHttpSpi] invoke error, un-catch throwable is: ", e);
            }
            throw new CapaException(CapaErrorContext.SYSTEM_ERROR, e);
        }
    }

    private <T> void callbackLog(HttpResponse<T> tHttpResponse, Throwable throwable) {
        if (throwable != null) {
            if (throwable instanceof CapaException) {
                return;
            }
            // un-catch throwable
            else {
                if (logger.isErrorEnabled()) {
                    logger.error("[Capa.Rpc.Client.http.callback] [CapaHttpSpi] invoke rpc response error",
                            throwable);
                }
                return;
            }
        }
        if (tHttpResponse == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("[Capa.Rpc.Client.http.callback] [CapaHttpSpi] invoke rpc response empty.");
            }
            return;
        }
        final int responseStatusCode = tHttpResponse.getStatusCode();
        final Map<String, String> responseHeaders = tHttpResponse.getHeaders();
        final T responseBody = tHttpResponse.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug("[Capa.Rpc.Client.http.callback] [CapaHttpSpi] invoke rpc response code[{}] headers[{}] body[{}]",
                    responseStatusCode, responseHeaders, responseBody);
        }
    }

    /**
     * Invoke spi api and then return async completable future.
     *
     * @param <T>           the response type parameter
     * @param appId         the app id
     * @param method        the invoke method
     * @param requestData   the request data
     * @param httpMethod    the http method
     * @param headers       the headers
     * @param urlParameters the url parameters
     * @param type          the response type
     * @return the async completable future
     */
    protected abstract <T> CompletableFuture<HttpResponse<T>> invokeSpiApi(String appId,
                                                                           String method,
                                                                           Object requestData,
                                                                           String httpMethod,
                                                                           Map<String, String> headers,
                                                                           Map<String, List<String>> urlParameters,
                                                                           TypeRef<T> type);
}
