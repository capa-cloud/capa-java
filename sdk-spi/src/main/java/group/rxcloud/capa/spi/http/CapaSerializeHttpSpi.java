package group.rxcloud.capa.spi.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.domain.core.invocation.Metadata;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The type Capa serialize http spi.
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
     * Gets request serialization format.
     *
     * @param requestData the request data
     * @return the request with byte[] serialize
     */
    protected byte[] getRequestWithSerialize(Object requestData) {
        try {
            return objectSerializer.serialize(requestData);
        } catch (IOException e) {
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_REQUEST_SERIALIZE_ERROR,
                    "Request Type: " + requestData.getClass().getName());
        } catch (Exception e) {
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_REQUEST_SERIALIZE_ERROR,
                    "Request Type: " + requestData.getClass().getName(), e);
        }
    }

    /**
     * Gets request body with byte[] serialize.
     *
     * @param requestData the request data
     * @param headers     the headers
     * @return the request body with byte[] serialize
     */
    protected RequestBody getRequestBodyWithSerialize(Object requestData, Map<String, String> headers) {
        byte[] serializedRequestBody = getRequestWithSerialize(requestData);
        final String contentType = headers != null
                ? headers.get(Metadata.CONTENT_TYPE)
                : null;
        final MediaType mediaType = contentType == null
                ? MEDIA_TYPE_APPLICATION_JSON
                : MediaType.get(contentType);
        RequestBody body;
        if (requestData == null) {
            body = mediaType.equals(MEDIA_TYPE_APPLICATION_JSON)
                    ? REQUEST_BODY_EMPTY_JSON
                    : RequestBody.Companion.create(new byte[0], mediaType);
        } else {
            body = RequestBody.Companion.create(serializedRequestBody, mediaType);
        }
        return body;
    }

    /**
     * Http async call
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
     * @param type         the type
     * @param httpResponse the http response
     * @return the response body with byte[] deserialize
     */
    protected <T> HttpResponse<T> getResponseBodyWithDeserialize(TypeRef<T> type, HttpResponse<byte[]> httpResponse) {
        final byte[] httpResponseBody = httpResponse.getBody();
        try {
            T responseObject = objectSerializer.deserialize(httpResponseBody, type);
            return new HttpResponse<>(responseObject, httpResponse.getHeaders(), httpResponse.getStatusCode());
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("[CapaSerializeHttpSpi] deserialize rpc response[{}] type[{}] io error",
                        httpResponseBody, type, e);
            }
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_RESPONSE_DESERIALIZE_ERROR,
                    "Response Type: " + type, e);
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("[CapaSerializeHttpSpi] deserialize rpc response[{}] type[{}] error",
                        httpResponseBody, type, e);
            }
            throw new CapaException(CapaErrorContext.PARAMETER_RPC_RESPONSE_DESERIALIZE_ERROR,
                    "Response Type: " + type, e);
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

        /**
         * JSON Object Mapper.
         */
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

            Map<String, String> mapHeaders = new HashMap<>();
            // response.headers()
            //         .forEach(pair -> mapHeaders.put(pair.getFirst(), pair.getSecond()));
            HttpResponse<byte[]> httpResponse = new HttpResponse<>(bodyBytes, mapHeaders, response.code());
            future.complete(httpResponse);
        }

        private void onResponseError(Response response, byte[] bodyBytes) {
            try {
                CapaException error = parseCapaError(bodyBytes);
                if (error != null) {
                    if (error.getErrorCodeContext() != null) {
                        future.completeExceptionally(new CapaException(error.getErrorCodeContext(),
                                "HTTP status code: " + response.code()));
                        return;
                    }
                }
                future.completeExceptionally(new CapaException(CapaErrorContext.DEPENDENT_SERVICE_ERROR,
                        "HTTP status code: " + response.code()));
            } catch (CapaException e) {
                future.completeExceptionally(e);
            }
        }

        private static byte[] getBodyBytesOrEmptyArray(Response response) throws IOException {
            ResponseBody body = response.body();
            if (body != null) {
                return body.bytes();
            }
            return EMPTY_BYTES;
        }

        /**
         * Tries to parse an error from Capa response body.
         *
         * @param json Response body from Capa remote.
         * @return CapaError or null if could not parse.
         */
        private static CapaException parseCapaError(byte[] json) {
            if ((json == null) || (json.length == 0)) {
                return null;
            }
            try {
                return OBJECT_MAPPER.readValue(json, CapaException.class);
            } catch (IOException e) {
                String errorMessage = new String(json, StandardCharsets.UTF_8);
                throw new CapaException(CapaErrorContext.DEPENDENT_SERVICE_ERROR, errorMessage, e);
            } catch (Exception e) {
                String errorMessage = new String(json, StandardCharsets.UTF_8);
                throw new CapaException(CapaErrorContext.SYSTEM_ERROR, errorMessage, e);
            }
        }
    }
}
