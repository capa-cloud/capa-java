package group.rxcloud.capa;

import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapaClient extends DefaultCloudRuntimesClient {

    @Override
    List<String> registryNames();

    @Override
    default Mono<Void> shutdown() {
        return Mono.empty();
    }

    @Override
    void close();
}
