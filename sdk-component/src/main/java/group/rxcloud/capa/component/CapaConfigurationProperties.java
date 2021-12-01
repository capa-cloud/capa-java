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
package group.rxcloud.capa.component;

import group.rxcloud.capa.infrastructure.CapaProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Capa configuration component common properties.
 */
public interface CapaConfigurationProperties {

    abstract class Settings {

        private static List<String> storeNames = Collections.singletonList("");

        private static final String CONFIGURATION_COMPONENT_STORE_NAMES = "CONFIGURATION_COMPONENT_STORE_NAMES";

        static {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply("configuration-common");

            String storeNames = properties.getProperty(CONFIGURATION_COMPONENT_STORE_NAMES, "");
            if (storeNames != null) {
                String[] split = storeNames.split(",");
                Settings.storeNames = Arrays.asList((split));
            }
        }

        public static List<String> getStoreNames() {
            return storeNames;
        }

        private Settings() {
        }
    }
}
