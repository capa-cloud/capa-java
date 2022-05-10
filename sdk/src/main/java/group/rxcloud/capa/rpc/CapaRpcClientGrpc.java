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
package group.rxcloud.capa.rpc;

import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;
import group.rxcloud.cloudruntimes.domain.core.invocation.RegisterServerRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * TODO An adapter for the GRPC RPC Client.
 */
public class CapaRpcClientGrpc extends AbstractCapaRpcClient {

    public CapaRpcClientGrpc() {
        this.registryNames = new ArrayList<>(1);
        this.registryNames.add("grpc");
    }

    @Override
    public <T> Mono<T> invokeMethod(InvokeMethodRequest invokeMethodRequest, TypeRef<T> type) {
        return null;
    }

    @Override
    public <T, R> Mono<Boolean> registerMethod(String methodName, List<HttpExtension> httpExtensions, Function<T, R> onInvoke, Map<String, String> metadata) {
        return null;
    }

    @Override
    public Mono<Boolean> registerServer(RegisterServerRequest registerServerRequest) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
    }
}
