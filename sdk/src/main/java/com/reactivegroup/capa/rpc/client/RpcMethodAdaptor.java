package com.reactivegroup.capa.rpc.client;

import reactor.core.publisher.Mono;

public interface RpcMethodAdaptor<Request, Response> {

    String getServiceAppId();

    String methodName();

    Mono<Response> invokeMethod(Request request);
}
