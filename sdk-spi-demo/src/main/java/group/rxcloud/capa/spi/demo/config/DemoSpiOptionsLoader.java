package group.rxcloud.capa.spi.demo.config;

import group.rxcloud.capa.spi.config.CapaSpiOptionsLoader;

import java.util.Objects;

public class DemoSpiOptionsLoader implements CapaSpiOptionsLoader<DemoRpcServiceOptions> {

    @Override
    public DemoRpcServiceOptions loadRpcServiceOptions(String appId) {
        Objects.requireNonNull(appId, "appId");
        return new DemoRpcServiceOptions(appId);
    }
}
