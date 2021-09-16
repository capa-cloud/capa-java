package group.rxcloud.capa.pubsub;

import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.core.binding.InvokeBindingRequest;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CapaPubSubClient extends DefaultCloudRuntimesClient {

    @Override
    Mono<Void> invokeBinding(String bindingName, String operation, Object data);

    @Override
    Mono<byte[]> invokeBinding(String bindingName, String operation, byte[] data, Map<String, String> metadata);

    @Override
    <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, TypeRef<T> type);

    @Override
    <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, Class<T> clazz);

    @Override
    <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, Map<String, String> metadata, TypeRef<T> type);

    @Override
    <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, Map<String, String> metadata, Class<T> clazz);

    @Override
    <T> Mono<T> invokeBinding(InvokeBindingRequest request, TypeRef<T> type);

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
