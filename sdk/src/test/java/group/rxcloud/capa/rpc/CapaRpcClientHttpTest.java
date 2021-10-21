package group.rxcloud.capa.rpc;

import group.rxcloud.capa.component.http.CapaHttpBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

public class CapaRpcClientHttpTest {

    @Test
    public void testStructure_Success() {
        CapaRpcClientBuilder capaRpcClientBuilder = new CapaRpcClientBuilder();
        Assert.assertNotNull(capaRpcClientBuilder);

        Supplier<CapaHttpBuilder> capaHttpBuilderSupplier = () -> new CapaHttpBuilder();
        CapaRpcClientBuilder rpcClientBuilder = new CapaRpcClientBuilder(capaHttpBuilderSupplier);
        Assert.assertNotNull(rpcClientBuilder);
    }

    @Test
    public void testBuild_Success() {
        CapaRpcClientBuilder capaRpcClientBuilder = new CapaRpcClientBuilder();
        CapaRpcClient client = capaRpcClientBuilder.build();

        Assert.assertNotNull(client);
    }

}
