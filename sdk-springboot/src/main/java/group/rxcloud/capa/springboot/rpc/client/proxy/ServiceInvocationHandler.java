package group.rxcloud.capa.springboot.rpc.client.proxy;

import group.rxcloud.capa.infrastructure.exceptions.CapaErrorContext;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.metainfo.CapaEnvironment;
import group.rxcloud.capa.rpc.CapaRpcClient;
import group.rxcloud.capa.springboot.rpc.client.ServiceClientConfig;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.vrml.core.serialization.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ServiceInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(ServiceInvocationHandler.class);

    private final CapaRpcClient capaRpcClient;
    private final String serviceId;
    private final ServiceClientConfig clientConfig;

    ServiceInvocationHandler(CapaRpcClient capaRpcClient, String serviceId, ServiceClientConfig clientConfig) {
        this.capaRpcClient = capaRpcClient;
        this.serviceId = adapterServiceId(serviceId);
        this.clientConfig = clientConfig;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    }

    private String adapterServiceId(String serviceId) {

        return serviceId;
    }

    private Mono<?> setCallTimeout(Mono<?> responseMono) {
        if (clientConfig != null) {
            int requestTimeoutInMilliseconds = clientConfig.getRequestTimeoutInMilliseconds();
            if (requestTimeoutInMilliseconds > 0) {
                Duration timeout = Duration.of(requestTimeoutInMilliseconds, ChronoUnit.MILLIS);
                responseMono = responseMono.timeout(timeout);
            }
        }
        return responseMono;
    }
}
