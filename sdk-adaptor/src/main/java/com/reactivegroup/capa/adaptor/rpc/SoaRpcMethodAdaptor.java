package com.reactivegroup.capa.adaptor.rpc;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

@Component
public class SoaRpcMethodAdaptor extends AbstractSoaRpcMethodAdaptor<SoaRpcEntity.RpcRequest, SoaRpcEntity.RpcResponse> {

    @Override
    public String methodName() {
        return "sayHello";
    }

    @Override
    public Mono<SoaRpcEntity.RpcResponse> invokeMethod(SoaRpcEntity.RpcRequest rpcRequest) {
        HttpClient client = getClient();

        Mono<HttpClientResponse> responseMono = client.get()
                .response();

        Mono<SoaRpcEntity.RpcResponse> rpcResponseMono = responseMono
                .map(httpClientResponse -> {
                    int code = httpClientResponse.status().code();
                    String message = httpClientResponse.status().toString();

                    SoaRpcEntity.RpcResponse rpcResponse = new SoaRpcEntity.RpcResponse();
                    rpcResponse.setCode(String.valueOf(code));
                    rpcResponse.setMessage(message);
                    return rpcResponse;
                });
        return rpcResponseMono;
    }
}