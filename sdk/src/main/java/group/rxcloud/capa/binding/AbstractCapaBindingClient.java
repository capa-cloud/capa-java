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
package group.rxcloud.capa.binding;


import group.rxcloud.capa.AbstractCapaClient;
import group.rxcloud.capa.configuration.CapaConfigurationClient;
import group.rxcloud.capa.configuration.CapaConfigurationClientStore;
import group.rxcloud.cloudruntimes.domain.core.binding.InvokeBindingRequest;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SaveConfigurationRequest;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Abstract class with convenient methods common between client implementations.
 */
public abstract class AbstractCapaBindingClient
        extends AbstractCapaClient
        implements CapaBindingClient {

    @Override
    public Mono<Void> invokeBinding(String bindingName, String operation, Object data) {
        return null;
    }

    @Override
    public Mono<byte[]> invokeBinding(String bindingName, String operation, byte[] data, Map<String, String> metadata) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, TypeRef<T> type) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, Map<String, String> metadata, TypeRef<T> type) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeBinding(String bindingName, String operation, Object data, Map<String, String> metadata, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> Mono<T> invokeBinding(InvokeBindingRequest request, TypeRef<T> type) {
        return null;
    }
}
