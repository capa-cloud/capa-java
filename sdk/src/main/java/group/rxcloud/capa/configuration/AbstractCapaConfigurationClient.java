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
package group.rxcloud.capa.configuration;


import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SaveConfigurationRequest;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Abstract class with convenient methods common between client implementations.
 *
 * @see CapaConfigurationClientStore
 */
public abstract class AbstractCapaConfigurationClient implements CapaConfigurationClient {

    protected List<String> registryNames;

    @Override
    public List<String> registryNames() {
        return registryNames;
    }

    @Override
    public Mono<Void> saveConfiguration(SaveConfigurationRequest saveConfigurationRequest) {
        return Mono.error(new UnsupportedOperationException("[Capa] Unsupported save configuration"));
    }

    @Override
    public Mono<Void> deleteConfiguration(ConfigurationRequestItem configurationRequestItem) {
        return Mono.error(new UnsupportedOperationException("[Capa] Unsupported delete configuration"));
    }
}
