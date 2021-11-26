package group.rxcloud.capa.spi.demo;

import group.rxcloud.capa.infrastructure.CapaEnvironment;

public class DemoCapaEnvironment implements CapaEnvironment {

    @Override
    public String getDeployCloud() {
        return "DEMO";
    }

    @Override
    public String getDeployRegion() {
        return "DEMO";
    }

    @Override
    public String getDeployEnv() {
        return "DEMO";
    }
}
