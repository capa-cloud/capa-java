package group.rxcloud.capa.state;

import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.core.state.*;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapaStateClient extends DefaultCloudRuntimesClient {

    @Override
    <T> Mono<State<T>> getState(String storeName, State<T> state, TypeRef<T> type);

    @Override
    <T> Mono<State<T>> getState(String storeName, State<T> state, Class<T> clazz);

    @Override
    <T> Mono<State<T>> getState(String storeName, String key, TypeRef<T> type);

    @Override
    <T> Mono<State<T>> getState(String storeName, String key, Class<T> clazz);

    @Override
    <T> Mono<State<T>> getState(String storeName, String key, StateOptions options, TypeRef<T> type);

    @Override
    <T> Mono<State<T>> getState(String storeName, String key, StateOptions options, Class<T> clazz);

    @Override
    <T> Mono<State<T>> getState(GetStateRequest request, TypeRef<T> type);

    @Override
    <T> Mono<List<State<T>>> getBulkState(String storeName, List<String> keys, TypeRef<T> type);

    @Override
    <T> Mono<List<State<T>>> getBulkState(String storeName, List<String> keys, Class<T> clazz);

    @Override
    <T> Mono<List<State<T>>> getBulkState(GetBulkStateRequest request, TypeRef<T> type);

    @Override
    Mono<Void> executeStateTransaction(String storeName, List<TransactionalStateOperation<?>> operations);

    @Override
    Mono<Void> executeStateTransaction(ExecuteStateTransactionRequest request);

    @Override
    Mono<Void> saveBulkState(String storeName, List<State<?>> states);

    @Override
    Mono<Void> saveBulkState(SaveStateRequest request);

    @Override
    Mono<Void> saveState(String storeName, String key, Object value);

    @Override
    Mono<Void> saveState(String storeName, String key, String etag, Object value, StateOptions options);

    @Override
    Mono<Void> deleteState(String storeName, String key);

    @Override
    Mono<Void> deleteState(String storeName, String key, String etag, StateOptions options);

    @Override
    Mono<Void> deleteState(DeleteStateRequest request);

    @Override
    default Mono<Void> shutdown() {
        return Mono.empty();
    }

    @Override
    void close();
}
