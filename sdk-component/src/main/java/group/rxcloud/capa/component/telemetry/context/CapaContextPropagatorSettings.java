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
package group.rxcloud.capa.component.telemetry.context;

import io.opentelemetry.context.propagation.TextMapPropagator;

/**
 * Settings for capa context propagator.
 */
public interface CapaContextPropagatorSettings {

    String FILE_PATH = "/capa-context.json";

    /**
     * Replace the whole context config.
     *
     * @param config context config
     * @return current settings.
     */
    CapaContextPropagatorSettings setContextConfig(ContextConfig config);

    /**
     * Add one more processor to current context config.
     *
     * @param processor processor config
     * @return current settings.
     */
    CapaContextPropagatorSettings addContextPropagators(TextMapPropagator processor);

}
