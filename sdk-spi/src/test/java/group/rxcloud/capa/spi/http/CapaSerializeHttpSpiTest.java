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
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.ObjectSerializer;
import group.rxcloud.capa.spi.http.config.RpcServiceOptions;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.util.context.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CapaSerializeHttpSpiTest {

    private OkHttpClient okHttpClient;

    private TestCapaSerializeHttpSpi capaSerializeHttpSpi;

    private CapaObjectSerializer defaultObjectSerializer;

    @BeforeEach
    public void setUp() {
        okHttpClient = new OkHttpClient.Builder().build();
        defaultObjectSerializer = new DefaultObjectSerializer();
        capaSerializeHttpSpi = new TestCapaSerializeHttpSpi(okHttpClient, defaultObjectSerializer);
    }

    @Test
    public void testGetRequestWithSerialize_Success() throws IOException {
        byte[] serializerRequest = capaSerializeHttpSpi.getRequestWithSerialize("Object");
        String request = defaultObjectSerializer.deserialize(serializerRequest, TypeRef.STRING);

        Assertions.assertEquals("Object", request);
    }

    @Test
    public void testGetRequestWithSerialize_FailWhenThrowException() {
        capaSerializeHttpSpi = new TestCapaSerializeHttpSpi(okHttpClient, new TestIOExceptionObjectSerializer());
        Assertions.assertThrows(CapaException.class, () -> {
            capaSerializeHttpSpi.getRequestWithSerialize("Object");
        });

        capaSerializeHttpSpi = new TestCapaSerializeHttpSpi(okHttpClient, new TestRuntimeExceptionObjectSerializer());
        Assertions.assertThrows(CapaException.class, () -> {
            capaSerializeHttpSpi.getRequestWithSerialize("Object");
        });
    }

    @Test
    public void testGetRequestBodyWithSerialize_SuccessWhenHeaderHasValue() {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");

        RequestBody requestBody = capaSerializeHttpSpi.getRequestBodyWithSerialize("Object", headers);
        String type = requestBody.contentType().type();
        String subtype = requestBody.contentType().subtype();
        Assertions.assertEquals("application", type);
        Assertions.assertEquals("json", subtype);
    }

    @Test
    public void testGetRequestBodyWithSerialize_SuccessWhenHeaderIsNull() {
        RequestBody requestBody = capaSerializeHttpSpi.getRequestBodyWithSerialize(null, null);
        String type = requestBody.contentType().type();
        String subtype = requestBody.contentType().subtype();
        Assertions.assertEquals("application", type);
        Assertions.assertEquals("json", subtype);
    }

    @Test
    public void testGetRequestHeaderWithParams_SuccessWhenHeaderHasValue() {
        Map<String, String> headersParams = new HashMap<>();
        headersParams.put("key", "value");
        Headers requestHeaderWithParams = capaSerializeHttpSpi.getRequestHeaderWithParams(headersParams);
        String value = requestHeaderWithParams.get("key");
        Assertions.assertEquals("value", value);
    }

    @Test
    public void testGetRequestHeaderWithParams_SuccessWhenHeaderIsNull() {
        Headers requestHeaderWithParams = capaSerializeHttpSpi.getRequestHeaderWithParams(null);
        String value = requestHeaderWithParams.get("key");
        Assertions.assertNull(value);
    }

    @Test
    public void testDoAsyncInvoke0_Success() {
        Request request = new Request.Builder().url("https://www.url/").build();

        CompletableFuture<HttpResponse<String>> responseFuture = capaSerializeHttpSpi.doAsyncInvoke0(
                request,
                TypeRef.STRING);
        responseFuture.cancel(true);
    }

    @Test
    public void testGetResponseBodyWithDeserialize_Success() {
        HttpResponse<byte[]> httpResponse = new HttpResponse<>(null, null, 200);
        HttpResponse<String> deserializeHttpResponse = capaSerializeHttpSpi.getResponseBodyWithDeserialize(
                TypeRef.STRING,
                httpResponse);

        int statusCode = deserializeHttpResponse.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void testGetResponseBodyWithDeserialize_FailWhenThrowException() {
        HttpResponse<byte[]> httpResponse = new HttpResponse<>(null, null, 200);
        capaSerializeHttpSpi = new TestCapaSerializeHttpSpi(okHttpClient, new TestIOExceptionObjectSerializer());
        Assertions.assertThrows(CapaException.class, () -> {
            capaSerializeHttpSpi.getResponseBodyWithDeserialize(TypeRef.STRING, httpResponse);
        });

        capaSerializeHttpSpi = new TestCapaSerializeHttpSpi(okHttpClient, new TestRuntimeExceptionObjectSerializer());
        Assertions.assertThrows(CapaException.class, () -> {
            capaSerializeHttpSpi.getResponseBodyWithDeserialize(TypeRef.STRING, httpResponse);
        });
    }

    @Test
    public void testOnResponseInSerializationResponseFutureCallback_Success() throws IOException {
        CompletableFuture<HttpResponse<byte[]>> future = new CompletableFuture<>();
        CapaSerializeHttpSpi.SerializationResponseFutureCallback responseFutureCallback =
                new CapaSerializeHttpSpi.SerializationResponseFutureCallback(future);

        Request request = new Request.Builder().url("https://www.url/").build();
        ResponseBody responseBody = ResponseBody.create(new byte[1],
                MediaType.get("application/json; charset=utf-8"));

        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("message")
                .code(200)
                .build();
        responseFutureCallback.onResponse(null, response);
        future.cancel(true);

        response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("message")
                .code(200)
                .body(responseBody)
                .build();
        responseFutureCallback.onResponse(null, response);
        future.cancel(true);
    }


    @Test
    public void testGetRpcServiceOptions_Success() {
        RpcServiceOptions rpcServiceOptions = capaSerializeHttpSpi.getRpcServiceOptions("appId");
        String className = rpcServiceOptions.getClass().getName();
        Assertions.assertEquals("group.rxcloud.capa.spi.http.config.TestRpcServiceOptions", className);
    }

    @Test
    public void testDoInvokeApi_Success() throws ExecutionException, InterruptedException {
        String[] pathSegments = new String[]{
                CapaHttp.API_VERSION,
                "invoke", "appId",
                "method", "method"};

        Map<String, List<String>> urlParameters = new HashMap<>();
        urlParameters.put("key", new ArrayList<>());

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");

        Context context = Context.of("content", "value");

        CompletableFuture<HttpResponse<String>> responseCompletableFuture = capaSerializeHttpSpi.doInvokeApi(
                "httpMethod",
                pathSegments,
                urlParameters,
                "requestData",
                headers,
                context,
                TypeRef.STRING);

        HttpResponse<String> httpResponse = responseCompletableFuture.get();
        int statusCode = httpResponse.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    /**
     * serializer/deserializer for request/response objects used in tests only
     */
    private class TestRuntimeExceptionObjectSerializer extends ObjectSerializer implements CapaObjectSerializer {

        /**
         * {@inheritDoc}
         */
        @Override
        public byte[] serialize(Object o) {
            throw new RuntimeException("test serialize exception");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <T> T deserialize(byte[] data, TypeRef<T> type) {
            throw new RuntimeException("test deserialize exception");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getContentType() {
            return "";
        }
    }


    /**
     * serializer/deserializer for request/response objects used in tests only
     */
    private class TestIOExceptionObjectSerializer extends ObjectSerializer implements CapaObjectSerializer {

        /**
         * {@inheritDoc}
         */
        @Override
        public byte[] serialize(Object o) throws IOException {
            throw new IOException("test serialize ioexception");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <T> T deserialize(byte[] data, TypeRef<T> type) throws IOException {
            throw new IOException("test deserialize ioexception");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getContentType() {
            return "";
        }
    }


    /**
     * The test capa http invoker.
     */
    private class TestCapaSerializeHttpSpi extends CapaSerializeHttpSpi {

        public TestCapaSerializeHttpSpi(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
            super(httpClient, objectSerializer);
        }

        @Override
        protected <T> CompletableFuture<HttpResponse<T>> invokeSpiApi(String appId,
                                                                      String method,
                                                                      Object requestData,
                                                                      Map<String, String> headers,
                                                                      TypeRef<T> type,
                                                                      RpcServiceOptions rpcServiceOptions) {
            return CompletableFuture.supplyAsync(
                    () -> {
                        return new HttpResponse<>(null, null, 200);
                    },
                    Runnable::run);
        }
    }
}
