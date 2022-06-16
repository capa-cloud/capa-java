/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.state;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.domain.core.state.DeleteStateRequest;
import group.rxcloud.cloudruntimes.domain.core.state.ExecuteStateTransactionRequest;
import group.rxcloud.cloudruntimes.domain.core.state.GetBulkStateRequest;
import group.rxcloud.cloudruntimes.domain.core.state.GetStateRequest;
import group.rxcloud.cloudruntimes.domain.core.state.SaveStateRequest;
import group.rxcloud.cloudruntimes.domain.core.state.State;
import group.rxcloud.cloudruntimes.domain.core.state.StateOptions;
import group.rxcloud.cloudruntimes.domain.core.state.TransactionalStateOperation;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The Capa state client.
 */
public interface CapaStateClient extends CapaClient {

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
}
