package group.rxcloud.capa.spi.demo.http;

import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.spi.config.RpcServiceOptions;
import group.rxcloud.capa.spi.demo.config.DemoRpcServiceOptions;
import group.rxcloud.capa.spi.http.CapaSerializeHttpSpi;
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
