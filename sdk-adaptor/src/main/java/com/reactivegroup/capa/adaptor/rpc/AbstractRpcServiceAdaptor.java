package com.reactivegroup.capa.adaptor.rpc;

import com.reactivegroup.capa.rpc.RpcMethodAdaptor;
import com.reactivegroup.capa.rpc.RpcServiceAdaptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@SuppressWarnings("all")
public abstract class AbstractRpcServiceAdaptor<Client> implements RpcServiceAdaptor<Client> {

    private Map<String, RpcMethodAdaptor> methodAdaptorMap;
    private Client client;

    @PostConstruct
    public void build(List<RpcMethodAdaptor> methodAdaptorList) {
        buildMethods(methodAdaptorList);
        this.client = buildClient();
    }

    private void buildMethods(List<RpcMethodAdaptor> methodAdaptorList) {
        this.methodAdaptorMap = methodAdaptorList.stream()
                .filter(rpcMethodAdaptor -> getServiceAppId().equalsIgnoreCase(rpcMethodAdaptor.getServiceAppId()))
                .collect(Collectors.toMap(RpcMethodAdaptor::methodName, rpcMethodAdaptor -> rpcMethodAdaptor));
    }

    protected abstract Client buildClient();

    @Override
    public Map<String, RpcMethodAdaptor> getServiceMethodMap() {
        return this.methodAdaptorMap;
    }

    @Override
    public Client getServiceClient() {
        return this.client;
    }
}
