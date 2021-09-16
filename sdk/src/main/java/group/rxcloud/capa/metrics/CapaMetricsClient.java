package group.rxcloud.capa.metrics;

import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import reactor.core.publisher.Mono;

public interface CapaMetricsClient extends DefaultCloudRuntimesClient {

    @Override
    default Mono<Void> shutdown() {
        return Mono.empty();
    }

    @Override
    void close();
}
