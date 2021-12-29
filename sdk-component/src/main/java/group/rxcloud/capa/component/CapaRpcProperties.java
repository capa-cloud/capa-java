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

import java.util.Properties;

/**
 * Capa rpc component common properties.
 */
public interface CapaRpcProperties {

    abstract class Settings {

        /**
         * Determines if Capa client will use HTTP or Other client.
         */
        public static String API_PROTOCOL;

        /**
         * Capa's timeout in seconds for HTTP client reads.
         */
        public static Integer HTTP_CLIENT_READ_TIMEOUT_SECONDS;

        /**
         * Capa's default use of HTTP.
         */
        private static final String DEFAULT_API_PROTOCOL = "HTTP";

        /**
         * Capa's default timeout in seconds for HTTP client reads.
         */
        private static final Integer DEFAULT_HTTP_CLIENT_READTIMEOUTSECONDS = 60;

        static {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply("rpc");

            API_PROTOCOL = properties.getProperty("API_PROTOCOL", DEFAULT_API_PROTOCOL);

            String httpClientReadTimeoutSeconds = properties.getProperty("HTTP_CLIENT_READ_TIMEOUT_SECONDS", String.valueOf(DEFAULT_HTTP_CLIENT_READTIMEOUTSECONDS));
            HTTP_CLIENT_READ_TIMEOUT_SECONDS = Integer.valueOf(httpClientReadTimeoutSeconds);
        }

        public static String getApiProtocol() {
            return API_PROTOCOL;
        }

        public static Integer getHttpClientReadTimeoutSeconds() {
            return HTTP_CLIENT_READ_TIMEOUT_SECONDS;
        }

        private Settings() {
        }
    }
}
