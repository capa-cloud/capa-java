package group.rxcloud.capa.rpc;

import group.rxcloud.capa.rpc.domain.InvokeMethodRequestBuilder;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Abstract class with convenient methods common between client implementations.
 *
 * @see CapaRpcClientHttp
 */
public abstract class AbstractCapaRpcClient implements CapaRpcClient {

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata, TypeRef<T> type) {
        InvokeMethodRequestBuilder builder = new InvokeMethodRequestBuilder(appId, methodName);
        InvokeMethodRequest req = builder
                .withBody(request)
                .withHttpExtension(httpExtension)
                .withMetadata(metadata)
                .build();

        return this.invokeMethod(req, type);
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata, Class<T> clazz) {
        return this.invokeMethod(appId, methodName, request, httpExtension, metadata, TypeRef.get(clazz));
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, TypeRef<T> type) {
        return this.invokeMethod(appId, methodName, request, httpExtension, null, type);
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Class<T> clazz) {
        return this.invokeMethod(appId, methodName, request, httpExtension, null, TypeRef.get(clazz));
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata, TypeRef<T> type) {
        return this.invokeMethod(appId, methodName, null, httpExtension, metadata, type);
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata, Class<T> clazz) {
        return this.invokeMethod(appId, methodName, null, httpExtension, metadata, TypeRef.get(clazz));
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata) {
        return this.invokeMethod(appId, methodName, request, httpExtension, metadata, TypeRef.VOID);
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension) {
        return this.invokeMethod(appId, methodName, request, httpExtension, null, TypeRef.VOID);
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata) {
        return this.invokeMethod(appId, methodName, null, httpExtension, metadata, TypeRef.VOID);
    }

    @Override
    public Mono<byte[]> invokeMethod(String appId, String methodName, byte[] request, HttpExtension httpExtension, Map<String, String> metadata) {
        return this.invokeMethod(appId, methodName, request, httpExtension, metadata, TypeRef.BYTE_ARRAY);
    }
}
