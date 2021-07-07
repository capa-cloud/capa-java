package com.reactivegroup.capa.adaptor.rpc;

import reactor.netty.http.client.HttpClient;

public abstract class AbstractSoaRpcMethodAdaptor<Request, Response> extends AbstractRpcMethodAdaptor<Request, Response> {

    @Override
    public String getServiceAppId() {
        return "hello";
    }

    @Override
    protected HttpClient getClient() {
        return super.getClient();
    }
}
