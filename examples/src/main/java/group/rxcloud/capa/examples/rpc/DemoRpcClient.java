package group.rxcloud.capa.examples.rpc;

import group.rxcloud.capa.rpc.CapaRpcClient;
import group.rxcloud.capa.rpc.CapaRpcClientBuilder;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

public class DemoRpcClient {

    /**
     * Identifier in Capa for the service this client will invoke.
     */
    private static final String SERVICE_APP_ID = "test";

    public static void main(String[] args) {
        CapaRpcClient capaRpcClient = new CapaRpcClientBuilder().build();

        Mono<byte[]> responseMono = capaRpcClient.invokeMethod(SERVICE_APP_ID, "hello", "hello", HttpExtension.POST, null, TypeRef.BYTE_ARRAY);

        byte[] response = responseMono.block();

        System.out.println(new String(response));
    }
}
