package group.rxcloud.capa.binding;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.core.binding.InvokeBindingRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * The Capa binding client.
 */
public interface CapaBindingClient extends CapaClient {

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
}
