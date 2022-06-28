package group.rxcloud.capa.springboot.rpc.client.proxy;

import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.springboot.rpc.client.ServiceClientConfig;
import group.rxcloud.capa.springboot.rpc.client.ServiceClientConfigBuilder;
import group.rxcloud.capa.springboot.rpc.client.plugin.CapaRpcFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

/**
 * Generate Capa proxy objects for The interface Client and initiate RPC calls through the Capa SDK.
 */
public class CapaRpcProxyFactory implements CapaRpcFactory {

    private final Logger log = LoggerFactory.getLogger(CapaRpcProxyFactory.class);

    @Override
    public <T> T getInstance(Class<T> interfaceClazz, String serviceId, Consumer<ServiceClientConfigBuilder> clientConfigBuilderConsumer) {
        final String interfaceClazzSimpleName = interfaceClazz.getSimpleName();

        Object interfaceProxyObj;
        try {
            Class[] interfaceClazzes = {interfaceClazz};
            ClassLoader interfaceClazzClassLoader = interfaceClazz.getClassLoader();
            Class interfaceProxyClazz = Proxy.getProxyClass(interfaceClazzClassLoader, interfaceClazzes);

            Class[] invocationHandlerClazz = {InvocationHandler.class};
            Constructor interfaceProxyConstructor = interfaceProxyClazz.getConstructor(invocationHandlerClazz);

            ServiceClientConfig config = null;
            if (clientConfigBuilderConsumer != null) {
                ServiceClientConfigBuilder configBuilder = new ServiceClientConfigBuilder();
                clientConfigBuilderConsumer.accept(configBuilder);
                config = configBuilder.build();
            }

            InvocationHandler invocationHandler = new ServiceInvocationHandler(capaRpcClient, serviceId, config);
            interfaceProxyObj = interfaceProxyConstructor.newInstance(invocationHandler);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("[Capa.Rpc.Client] [RpcClientFactory.getInstance] interface[{}] instance init catches error.",
                        interfaceClazzSimpleName, e);
            }
            throw new CapaException(CapaErrorContext.SYSTEM_ERROR, "CapaRpcClient getInstance() catches error", e);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[Capa.Rpc.Client] [RpcClientFactory.getInstance] interface[{}] instance init catches error.",
                        interfaceClazzSimpleName, e);
            }
            throw new CapaException(CapaErrorContext.SYSTEM_ERROR, "CapaRpcClient getInstance() error", e);
        }

        if (log.isInfoEnabled()) {
            log.info("[Capa.Rpc.Client] [RpcClientFactory.getInstance] interface[{}] instance has been init.", interfaceClazzSimpleName);
        }
        return (T) interfaceProxyObj;
    }
}
