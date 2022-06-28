package group.rxcloud.capa.springboot.rpc.client.plugin;


import group.rxcloud.capa.springboot.rpc.client.ServiceClientConfigBuilder;

import java.util.function.Consumer;

/**
 * Inner Factory.
 */
public interface CapaRpcFactory {

    /**
     * Gets rpc client instance.
     *
     * @param <T>                         the rpc contract type
     * @param interfaceClazz              the interface clazz
     * @param serviceId                   the service id
     * @param clientConfigBuilderConsumer the client config builder consumer
     * @return the client instance
     */
    <T> T getInstance(Class<T> interfaceClazz, String serviceId, Consumer<ServiceClientConfigBuilder> clientConfigBuilderConsumer);
}
