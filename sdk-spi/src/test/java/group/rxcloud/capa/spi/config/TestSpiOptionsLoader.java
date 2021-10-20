package group.rxcloud.capa.spi.config;

import java.util.Objects;

/**
 * The spi options loader used in tests only.
 */
public class TestSpiOptionsLoader implements CapaSpiOptionsLoader<TestRpcServiceOptions> {

    @Override
    public TestRpcServiceOptions loadRpcServiceOptions(String appId) {
        Objects.requireNonNull(appId, "appId");
        return new TestRpcServiceOptions(appId);
    }
}