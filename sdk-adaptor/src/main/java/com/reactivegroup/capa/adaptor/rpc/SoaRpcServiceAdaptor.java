package com.reactivegroup.capa.adaptor.rpc;

import org.springframework.stereotype.Component;
import reactor.netty.http.client.HttpClient;

@Component
public class SoaRpcServiceAdaptor extends AbstractRpcServiceAdaptor<HttpClient> {

    @Override
    public String getServiceAppId() {
        return "soa";
    }

    @Override
    protected HttpClient buildClient() {
        return HttpClient.create()
                .baseUrl("http://www.baidu.com");
    }
}
