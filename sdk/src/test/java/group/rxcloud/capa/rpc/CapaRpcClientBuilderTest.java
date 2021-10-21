package group.rxcloud.capa.rpc;

import group.rxcloud.capa.component.http.CapaHttp;
import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.capa.rpc.domain.InvokeMethodRequestBuilder;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class CapaRpcClientBuilderTest {

    private CapaRpcClientHttp capaRpcClientHttp;

    private OkHttpClient okHttpClient;

    private DefaultObjectSerializer defaultObjectSerializer;

    @Before
    public void setUp() {
        okHttpClient = new OkHttpClient.Builder().build();
        defaultObjectSerializer = new DefaultObjectSerializer();
        capaRpcClientHttp = new CapaRpcClientHttp(new TestCapaHttp(okHttpClient, defaultObjectSerializer));
    }

    @Test
    public void testInvokeMethod_FailWhenAppIdIsEmpty() {
        InvokeMethodRequest methodRequest = new InvokeMethodRequestBuilder("", "method").build();

        Mono<String> stringMono = capaRpcClientHttp.invokeMethod(methodRequest, TypeRef.STRING);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            stringMono.block();
        });
    }

    @Test
    public void testInvokeMethod_FailWhenMethodIsEmpty() {
        InvokeMethodRequest methodRequest = new InvokeMethodRequestBuilder("appId", "").build();

        Mono<String> stringMono = capaRpcClientHttp.invokeMethod(methodRequest, TypeRef.STRING);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            stringMono.block();
        });
    }

    @Test
    public void testInvokeMethod_FailWhenHttpExtensionIsNull() {
        InvokeMethodRequest methodRequest = new InvokeMethodRequestBuilder("appId", "method")
                .withHttpExtension(null)
                .build();

        Mono<String> stringMono = capaRpcClientHttp.invokeMethod(methodRequest, TypeRef.STRING);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            stringMono.block();
        });
    }

    @Test
    public void testInvokeMethod_Success() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        InvokeMethodRequest methodRequest = new InvokeMethodRequestBuilder("appId", "method")
                .withHttpExtension(HttpExtension.POST)
                .withMetadata(metadata)
                .build();

        Mono<String> responseMono = capaRpcClientHttp.invokeMethod(methodRequest, TypeRef.STRING);
        String response = responseMono.block();

        Assert.assertNull(response);
    }

    @Test
    public void testAbstractInvokeMethod_Success() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        Mono<String> responseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                "request",
                HttpExtension.POST,
                metadata,
                String.class);
        String response = responseMono.block();
        Assert.assertNull(response);

        responseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                "request",
                HttpExtension.POST,
                TypeRef.STRING);
        response = responseMono.block();
        Assert.assertNull(response);

        responseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                "request",
                HttpExtension.POST,
                String.class);
        response = responseMono.block();
        Assert.assertNull(response);

        responseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                HttpExtension.POST,
                metadata,
                String.class);
        response = responseMono.block();
        Assert.assertNull(response);

        responseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                HttpExtension.POST,
                metadata,
                TypeRef.STRING);
        response = responseMono.block();
        Assert.assertNull(response);

        Mono<Void> voidResponseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                "request",
                HttpExtension.POST,
                metadata);
        voidResponseMono.block();

        voidResponseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                "request",
                HttpExtension.POST);
        voidResponseMono.block();

        voidResponseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                HttpExtension.POST,
                metadata);
        voidResponseMono.block();

        Mono<byte[]> byteResponseMono = capaRpcClientHttp.invokeMethod("appId",
                "method",
                new byte[1],
                HttpExtension.POST,
                metadata);
        byte[] byteResponse = byteResponseMono.block();
        Assert.assertNull(byteResponse);

    }

    @Test
    public void testShutdown_Success() {
        Mono<Void> shutdown = capaRpcClientHttp.shutdown();
        shutdown.block();
    }

    @Test
    public void testClose_Success() {
        capaRpcClientHttp.close();
    }

    @Test
    public void testClose_FailWhenThrowException() {

        capaRpcClientHttp = new CapaRpcClientHttp(new ExceptionCapaHttp(okHttpClient, defaultObjectSerializer));

        Assert.assertThrows(CapaException.class, () -> {
            capaRpcClientHttp.close();
        });
    }

    /**
     * The capa http invoker used in tests only.
     */
    private class ExceptionCapaHttp extends CapaHttp {

        public ExceptionCapaHttp(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
            super(httpClient, objectSerializer);
        }

        @Override
        protected <T> CompletableFuture<HttpResponse<T>> doInvokeApi(String httpMethod,
                                                                     String[] pathSegments,
                                                                     Map<String, List<String>> urlParameters,
                                                                     Object requestData,
                                                                     Map<String, String> headers,
                                                                     Context context,
                                                                     TypeRef<T> type) {
            return null;
        }

        @Override
        public void close() {
            throw new RuntimeException() {
            };
        }
    }

}
