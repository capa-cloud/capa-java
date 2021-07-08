package com.reactivegroup.capa.adaptor.rpc;

import com.reactivegroup.capa.rpc.client.RpcMethodAdaptor;
import com.reactivegroup.capa.rpc.client.RpcServiceAdaptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@SuppressWarnings("all")
public abstract class AbstractRpcMethodAdaptor<Request, Response> implements RpcMethodAdaptor<Request, Response> {

    private RpcServiceAdaptor serviceAdaptor;

    @PostConstruct
    public void build(List<RpcServiceAdaptor> serviceAdaptorList) {
        this.serviceAdaptor = serviceAdaptorList.stream()
                .filter(rpcServiceAdaptor -> getServiceAppId().equals(rpcServiceAdaptor.getServiceAppId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("method no service"));
    }

    protected <Client> Client getClient() {
        return (Client) serviceAdaptor.getServiceClient();
    }
}
