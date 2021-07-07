package com.reactivegroup.capa.rpc;

import reactor.core.publisher.Mono;

public interface RpcMethodAdaptor<Request, Response> {

    String getServiceAppId();

    String methodName();

    Mono<Response> invokeMethod(Request request);
}
