package group.rxcloud.capa.springboot.rpc.client;

import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.plugin.PluginLoader;
import group.rxcloud.capa.springboot.rpc.client.plugin.CapaRpcFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Rpc client factory.
 */
public interface CapaRpcClientFactory {

    Logger log = LoggerFactory.getLogger(CapaRpcClientFactory.class);

    /**
     * Gets rpc client proxy instance.
     *
     * @param <T>            the rpc contract type
     * @param interfaceClazz the interface clazz
     * @param serviceId      the service id
     * @return the proxy instance
     */
    static <T> T getInstance(Class<T> interfaceClazz, String serviceId) {
        return getInstance(interfaceClazz, serviceId, null);
    }

    /**
     * Gets rpc client proxy instance.
     *
     * @param <T>                         the rpc contract type
     * @param interfaceClazz              the interface clazz
     * @param serviceId                   the service id
     * @param clientConfigBuilderConsumer the client config builder consumer
     * @return the proxy instance
     */
    static <T> T getInstance(Class<T> interfaceClazz, String serviceId, Consumer<ServiceClientConfigBuilder> clientConfigBuilderConsumer) {
        Optional<CapaRpcFactory> capaRpcFactory = PluginLoader.loadPluginImpl(CapaRpcFactory.class);
        if (!capaRpcFactory.isPresent()) {
            if (log.isErrorEnabled()) {
                log.error("[Capa.Rpc.Client] [RpcClientFactory.getInstance] can't found rpc client factory.");
            }
            throw new CapaException(CapaErrorContext.SYSTEM_ERROR, "CapaRpcClient get factory error");
        }
        return capaRpcFactory.get()
                .getInstance(interfaceClazz, serviceId, clientConfigBuilderConsumer);
    }
}
