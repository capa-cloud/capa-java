package group.rxcloud.capa.spi.http;

import group.rxcloud.capa.component.http.CapaHttp;
import group.rxcloud.capa.component.http.HttpResponse;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.spi.config.CapaSpiProperties;
import group.rxcloud.capa.spi.config.CapaSpiOptionsLoader;
import group.rxcloud.capa.spi.config.RpcServiceOptions;
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
     * @param httpMethod    Ignore, fix to POST. TODO
     * @param urlParameters Ignore, fix to EMPTY. TODO
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
