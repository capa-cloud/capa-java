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

import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.domain.core.invocation.Metadata;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static group.rxcloud.cloudruntimes.domain.core.invocation.Metadata.ACCEPT;

/**
 * The Capa http spi with default serializer process.
 */
public abstract class CapaSerializeHttpSpi extends CapaHttpSpi {

    private static final Logger logger = LoggerFactory.getLogger(CapaSerializeHttpSpi.class);

    /**
     * Instantiates a new Capa serialize http spi.
     *
     * @param httpClient       the http client
     * @param objectSerializer the object serializer
     */
    public CapaSerializeHttpSpi(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
        super(httpClient, objectSerializer);
    }

    /**
     * HTTP invoke facade.
     */
    protected <T> CompletableFuture<HttpResponse<T>> invokeHttpFacade(String url,
                                                                      Object requestData,
                                                                      String httpMethod,
                                                                      Map<String, String> headers,
                                                                      Map<String, List<String>> urlParameters,
                                                                      TypeRef<T> type) {
        // get http header: content-type
        final String contentType = getRequestContentType(headers);

        // generate http request body
        RequestBody body = getRequestBodyWithSerialize(requestData, contentType);

        // get http header: accept
        getRequestAcceptType(headers, body);

        // generate http headers
        Headers header = getRequestHeaderWithParams(headers);

        if (logger.isDebugEnabled()) {
            logger.debug("[Capa.Rpc.Client.http] [CapaSerializeHttpSpi] final request url[{}] header[{}] httpMethod[{}]",
                    url, header, httpMethod);
        }

        // make http request
        Request request = new Request.Builder()
                .url(url)
                .headers(header)
                .method(httpMethod, body)
                .build();

        CompletableFuture<HttpResponse<T>> asyncInvoke0 = doAsyncInvoke0(request, type);
        asyncInvoke0.exceptionally(throwable -> {
            if (throwable instanceof CapaException) {
                throw (CapaException) throwable;
            }
            // un-catch throwable
            else {
                if (logger.isErrorEnabled()) {
                    logger.error("[Capa.Rpc.Client.http.callback] [CapaSerializeHttpSpi] async invoke error, un-catch throwable is: ", throwable);
                }
                throw new CapaException(CapaErrorContext.SYSTEM_ERROR, throwable);
            }
        });
        return asyncInvoke0;
    }

    /**
     * Gets request content type.
     *
     * @param headers the headers with custom content type
     * @return the request content type
     */
    protected String getRequestContentType(Map<String, String> headers) {
        if (headers != null) {
            String contentType = headers.get(Metadata.CONTENT_TYPE);
            if (contentType != null) {
                return contentType;
            }
        }
        return this.objectSerializer.getContentType();
    }

    /**
     * Gets request body with byte[] serialize.
     *
     * @param requestData the request data
     * @return the request body with byte[] serialize
     */
    protected RequestBody getRequestBodyWithSerialize(Object requestData, String contentType) {
        final MediaType mediaType = contentType == null
                ? MEDIA_TYPE_APPLICATION_JSON
                : MediaType.get(contentType);

        RequestBody body;
        if (requestData == null) {
            body = mediaType.equals(MEDIA_TYPE_APPLICATION_JSON)
                    ? REQUEST_BODY_EMPTY_JSON
                    : RequestBody.Companion.create(new byte[0], mediaType);
        } else {
            byte[] serializedRequestBody = getRequestWithSerialize(requestData);
            body = RequestBody.Companion.create(serializedRequestBody, mediaType);
        }

        return body;
    }

    /**
     * Gets request serialization format.
     *
     * @param requestData the request data
     * @return the request with byte[] serialize
     */
    protected byte[] getRequestWithSerialize(Object requestData) {
        try {
            return objectSerializer.serialize(requestData);
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("[Capa.Rpc.Client.http] [CapaSerializeHttpSpi] serialize rpc request[{}] io error",
                        requestData, e);
            }
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_REQUEST_SERIALIZE_ERROR,
                    "Request Type: " + requestData.getClass().getName() + ", IO Error: " + e.getMessage());
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("[Capa.Rpc.Client.http] [CapaSerializeHttpSpi] serialize rpc request[{}] error",
                        requestData, e);
            }
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_REQUEST_SERIALIZE_ERROR,
                    "Request Type: " + requestData.getClass().getName() + ", Error: " + e.getMessage(), e);
        }
    }

    /**
     * Gets request accept type.
     *
     * @param headers the headers with custom value
     * @param body    request body
     */
    protected void getRequestAcceptType(Map<String, String> headers, RequestBody body) {
        final List<String> accepts = new ArrayList<>(3);
        // 1. set user accept header
        final String userAcceptValue = headers.get(ACCEPT);
        if (userAcceptValue != null && userAcceptValue.length() > 0) {
            accepts.add(userAcceptValue);
        }
        // 2. set accept header same with content-type
        if (body.contentType() != null) {
            final String contentType = Objects.requireNonNull(body.contentType()).toString();
            if (contentType.length() > 0) {
                accepts.add(contentType);
            }
        }
        // 3. add */* at last
        accepts.add("*/*");

        final String acceptStr = accepts.stream()
                .distinct()
                .collect(Collectors.joining(","));
        headers.put(ACCEPT, acceptStr);
    }

    /**
     * Gets request headers with given params.
     *
     * @param headersParams user given params
     * @return the request headers
     */
    protected Headers getRequestHeaderWithParams(Map<String, String> headersParams) {
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
        if (headersParams == null || headersParams.size() == 0) {
            return headersBuilder.build();
        }
        headersParams.forEach(headersBuilder::add);
        return headersBuilder.build();
    }

    /**
     * Http async call
     *
     * @param <T>     the type parameter
     * @param request the request
     * @param type    the type
     * @return the completable future
     */
    protected <T> CompletableFuture<HttpResponse<T>> doAsyncInvoke0(Request request, TypeRef<T> type) {
        CompletableFuture<HttpResponse<byte[]>> future = new CompletableFuture<>();
        SerializationResponseFutureCallback responseFutureCallback = new SerializationResponseFutureCallback(future);

        try {
            httpClient.newCall(request).enqueue(responseFutureCallback);
        } catch (Exception e) {
            // Unexpected synchronization error, for debugging
            throw e;
        }

        CompletableFuture<HttpResponse<T>> responseFuture = future.thenApply(
                httpResponse -> getResponseBodyWithDeserialize(type, httpResponse));
        return responseFuture;
    }

    /**
     * Gets response body with byte[] deserialize.
     *
     * @param <T>          the type parameter
     * @param type         the response type
     * @param httpResponse the http response
     * @return the response body with byte[] deserialize
     */
    protected <T> HttpResponse<T> getResponseBodyWithDeserialize(TypeRef<T> type, HttpResponse<byte[]> httpResponse) {
        final int httpResponseStatusCode = httpResponse.getStatusCode();
        final Map<String, String> httpResponseHeaders = httpResponse.getHeaders();
        final byte[] httpResponseBody = httpResponse.getBody();
        try {
            T responseObject = objectSerializer.deserialize(httpResponseBody, type);
            return new HttpResponse<>(responseObject, httpResponseHeaders, httpResponseStatusCode);
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("[Capa.Rpc.Client.http.callback] [CapaSerializeHttpSpi] deserialize response statusCode[{}] headers[{}] response[{}] type[{}] io error",
                        httpResponseStatusCode, httpResponseHeaders, httpResponseBody, type.getType().getTypeName(), e);
            }
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_RESPONSE_DESERIALIZE_ERROR,
                    "Response Type: " + type.getType().getTypeName() + ", IO Error: " + e.getMessage(), e);
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("[Capa.Rpc.Client.http.callback] [CapaSerializeHttpSpi] deserialize response statusCode[{}] headers[{}] response[{}] type[{}] error",
                        httpResponseStatusCode, httpResponseHeaders, httpResponseBody, type.getType().getTypeName(), e);
            }
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_RESPONSE_DESERIALIZE_ERROR,
                    "Response Type: " + type.getType().getTypeName() + ", Error: " + e.getMessage(), e);
        }
    }

    /**
     * The {@code byte[]} serialization response future callback.
     */
    protected static class SerializationResponseFutureCallback implements Callback, Serializable {

        /**
         * Empty input or output.
         */
        private static final byte[] EMPTY_BYTES = new byte[0];

        private final transient CompletableFuture<HttpResponse<byte[]>> future;

        /**
         * Instantiates a new Serialization response future callback.
         *
         * @param future the future
         */
        public SerializationResponseFutureCallback(CompletableFuture<HttpResponse<byte[]>> future) {
            this.future = future;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            future.completeExceptionally(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            byte[] bodyBytes = getBodyBytesOrEmptyArray(response);
            if (!response.isSuccessful()) {
                onResponseError(response, bodyBytes);
                return;
            }

            Map<String, String> mapHeaders;
            Headers responseHeaders = response.headers();
            if (responseHeaders == null || responseHeaders.size() == 0) {
                mapHeaders = new HashMap<>(2, 1);
            } else {
                mapHeaders = new HashMap<>(responseHeaders.size() << 1);
                responseHeaders.forEach(pair -> mapHeaders.put(pair.getFirst(), pair.getSecond()));
            }

            HttpResponse<byte[]> httpResponse = new HttpResponse<>(bodyBytes, mapHeaders, response.code());
            future.complete(httpResponse);
        }

        private static byte[] getBodyBytesOrEmptyArray(Response response) throws IOException {
            ResponseBody body = response.body();
            if (body != null) {
                return body.bytes();
            }
            return EMPTY_BYTES;
        }

        private void onResponseError(Response response, byte[] bodyBytes) {
            try {
                String error = parseCapaError(bodyBytes);
                if (logger.isWarnEnabled()) {
                    logger.warn("[Capa.Rpc.Client.http.callback] [FutureCallback.onResponseError] response statusCode[{}], errorMsg[{}]",
                            response.code(), error);
                }
                CapaException capaException = new CapaException(CapaErrorContext.DEPENDENT_SERVICE_ERROR,
                        "HTTP status code: " + response.code() + ", error message: " + error);
                future.completeExceptionally(capaException);
            } catch (CapaException e) {
                future.completeExceptionally(e);
            }
        }

        /**
         * Tries to parse an error from Capa response body.
         *
         * @param bytes Response body from Capa remote.
         * @return ErrorMsg or null if could not parse.
         */
        private static String parseCapaError(byte[] bytes) {
            if ((bytes == null) || (bytes.length == 0)) {
                return null;
            }
            try {
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("[Capa.Rpc.Client.http.callback] [FutureCallback.parseCapaError] bytes[{}] parse error",
                            bytes, e);
                }
                throw new CapaException(CapaErrorContext.SYSTEM_ERROR, e);
            }
        }
    }
}
