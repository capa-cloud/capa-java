package group.rxcloud.capa.pubsub;

import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CapaPubSubClient extends DefaultCloudRuntimesClient {

    @Override
    Mono<Void> publishEvent(String pubsubName, String topicName, Object data);

    @Override
    Mono<Void> publishEvent(String pubsubName, String topicName, Object data, Map<String, String> metadata);

    @Override
    Mono<Void> publishEvent(PublishEventRequest request);

    @Override
    default Mono<Void> shutdown() {
        return Mono.empty();
    }

    @Override
    void close();
}
