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

import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The Abstract Http Client. Extend this and provide your specific impl.
 */
public abstract class CapaHttp implements AutoCloseable {

    /**
     * Capa API used in this client.
     */
    public static final String API_VERSION = "v1.0";

    /**
     * Capa's http default scheme.
     */
    private static final String DEFAULT_HTTP_SCHEME = "http";

    /**
     * Defines the standard application/json type for HTTP calls in Capa.
     */
    protected static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
    /**
     * Defines the standard application/json type for HTTP calls in Capa.
     */
    protected static final MediaType MEDIA_TYPE_APPLICATION_JSON =
            MediaType.get(APPLICATION_JSON_CHARSET_UTF_8);
    /**
     * Shared object representing an empty request body in JSON.
     */
    protected static final RequestBody REQUEST_BODY_EMPTY_JSON =
            RequestBody.Companion.create("", MEDIA_TYPE_APPLICATION_JSON);

    /**
     * Http client used for all API calls.
     */
    protected final OkHttpClient httpClient;

    /**
     * A utility class for serialize and deserialize the transient objects.
     */
    protected final CapaObjectSerializer objectSerializer;

    /**
     * Instantiates a new Capa http.
     *
     * @param httpClient       RestClient used for all API calls in this new instance.
     * @param objectSerializer Serializer for transient request/response objects.
     * @see CapaHttpBuilder
     */
    public CapaHttp(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
        this.httpClient = httpClient;
        this.objectSerializer = objectSerializer;
    }

    /**
     * Invokes an API asynchronously that returns a {@code <T>} payload.
     *
     * @param <T>           The Type of the return.
     * @param httpMethod    HTTP httpMethod.
     * @param pathSegments  Array of path segments ("/a/b/c" maps to ["a", "b", "c"]).
     * @param urlParameters Parameters in the URL
     * @param requestData   payload to be posted.
     * @param headers       HTTP headers.
     * @param context       OpenTelemetry's Context.
     * @param type          The Type needed as return for the call.
     * @return Asynchronous response
     */
    public <T> Mono<HttpResponse<T>> invokeApi(String httpMethod,
                                               String[] pathSegments,
                                               Map<String, List<String>> urlParameters,
                                               Object requestData,
                                               Map<String, String> headers,
                                               Context context,
                                               TypeRef<T> type) {
        // fromCallable() is needed so the invocation does not happen early, causing a hot mono.
        return Mono.fromCallable(() -> doInvokeApi(httpMethod, pathSegments, urlParameters, requestData, headers, context, type))
                .flatMap(f -> Mono.fromFuture(f));
    }

    /**
     * Invokes an API that returns a {@code <T>} payload.
     * FIXME: expose {@code Mono}, not {@code CompletableFuture}
     *
     * @param <T>           The Type of the return.
     * @param httpMethod    HTTP httpMethod.
     * @param pathSegments  Array of path segments (/a/b/c to ["a", "b", "c"]).
     * @param urlParameters Parameters in the URL
     * @param requestData   payload to be posted.
     * @param headers       HTTP headers.
     * @param context       OpenTelemetry's Context.
     * @param type          The Type needed as return for the call.
     * @return CompletableFuture for Response.
     */
    protected abstract <T> CompletableFuture<HttpResponse<T>> doInvokeApi(String httpMethod,
                                                                          String[] pathSegments,
                                                                          Map<String, List<String>> urlParameters,
                                                                          Object requestData,
                                                                          Map<String, String> headers,
                                                                          Context context,
                                                                          TypeRef<T> type);

    /**
     * Shutdown call is not necessary for OkHttpClient.
     *
     * @see OkHttpClient
     */
    @Override
    public void close() throws Exception {
        // No code needed
    }
}
