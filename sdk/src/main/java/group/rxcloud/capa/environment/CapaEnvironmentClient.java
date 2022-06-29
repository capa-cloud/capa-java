package group.rxcloud.capa.environment;

import group.rxcloud.capa.CapaClient;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * The Capa environment client.
 */
public interface CapaEnvironmentClient extends CapaClient {

    @Override
    default String appId() {
        return CapaClient.super.appId();
    }

    @Override
    default String env() {
        return CapaClient.super.env();
    }

    @Override
    default String ip() {
        return CapaClient.super.ip();
    }

    @Override
    default String cloud() {
        return CapaClient.super.cloud();
    }

    @Override
    default String region() {
        return CapaClient.super.region();
    }

    @Override
    default String getProperty(String name, String defaultValue) {
        return CapaClient.super.getProperty(name, defaultValue);
    }
}
