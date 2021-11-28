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
package group.rxcloud.capa.infrastructure;

/**
 * The Capa infrastructure constants.
 */
interface CapaConstants {

    /**
     * The Properties constants.
     */
    interface Properties {

        /**
         * The {@code infrastructure} properties prefix.
         */
        String CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX = "/capa-infrastructure-";
        /**
         * The {@code component} properties prefix.
         */
        String CAPA_COMPONENT_PROPERTIES_PREFIX = "/capa-component-";

        /**
         * The properties suffix.
         */
        String CAPA_PROPERTIES_SUFFIX = ".properties";
        /**
         * The json suffix.
         */
        String CAPA_JSON_SUFFIX = ".json";
    }
}
