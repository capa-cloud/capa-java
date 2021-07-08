package com.reactivegroup.capa.rpc.client;

import com.reactivegroup.capa.domain.HttpExtension;
import com.reactivegroup.capa.domain.InvokeMethodRequest;
import com.reactivegroup.capa.domain.TypeRef;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

public class CapaRpcClientProxy implements CapaRpcClient {

    private Map<String, RpcServiceAdaptor> rpcAdaptor;

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object data, HttpExtension httpExtension, Map<String, String> metadata, TypeRef<T> type) {
        Objects.requireNonNull(rpcAdaptor);

        RpcServiceAdaptor serviceAdaptor = Objects.requireNonNull(rpcAdaptor.get(appId));

        Map<String, RpcMethodAdaptor> serviceMethodMap = Objects.requireNonNull(serviceAdaptor.getServiceMethodMap());

        RpcMethodAdaptor methodAdaptor = Objects.requireNonNull(serviceMethodMap.get(methodName));

        Mono invokeMethod = methodAdaptor.invokeMethod(data);

        return invokeMethod.flatMap(o -> Mono.just((T) o));
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, TypeRef<T> type) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata, TypeRef<T> type) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata, Class<T> clazz) {
        return null;
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension, Map<String, String> metadata) {
        return null;
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, Object request, HttpExtension httpExtension) {
        return null;
    }

    @Override
    public Mono<Void> invokeMethod(String appId, String methodName, HttpExtension httpExtension, Map<String, String> metadata) {
        return null;
    }

    @Override
    public Mono<byte[]> invokeMethod(String appId, String methodName, byte[] request, HttpExtension httpExtension, Map<String, String> metadata) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeMethod(InvokeMethodRequest invokeMethodRequest, TypeRef<T> type) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
