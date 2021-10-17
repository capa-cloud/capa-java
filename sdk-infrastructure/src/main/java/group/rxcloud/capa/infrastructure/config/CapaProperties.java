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
package group.rxcloud.capa.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Supplier;

import static group.rxcloud.capa.infrastructure.constants.CapaConstants.Properties.CAPA_COMPONENT_PROPERTIES;

/**
 * Global properties for Capa's SDK, using Supplier so they are dynamically resolved.
 * <p>
 * TODO Allow dynamic configuration
 */
public abstract class CapaProperties {

    /**
     * Capa's default use of HTTP.
     */
    private static final Supplier<String> DEFAULT_API_PROTOCOL = () -> "HTTP";

    /**
     * Determines if Capa client will use HTTP or Other client.
     */
    public static final Supplier<String> API_PROTOCOL = DEFAULT_API_PROTOCOL;

    /**
     * Capa's default timeout in seconds for HTTP client reads.
     */
    private static final Integer DEFAULT_HTTP_CLIENT_READTIMEOUTSECONDS = 60;

    /**
     * Capa's timeout in seconds for HTTP client reads.
     */
    public static final Supplier<Integer> HTTP_CLIENT_READ_TIMEOUT_SECONDS = () -> DEFAULT_HTTP_CLIENT_READTIMEOUTSECONDS;

    /**
     * Capa's component properties.
     */
    private static final Properties COMPONENT_PROPERTIES = loadCapaComponentProperties();

    /**
     * Capa's component properties.
     */
    public static final Supplier<Properties> COMPONENT_PROPERTIES_SUPPLIER = () -> COMPONENT_PROPERTIES;

    private static Properties loadCapaComponentProperties() {
        try (InputStream in = CapaProperties.class.getResourceAsStream(CAPA_COMPONENT_PROPERTIES)) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(inputStreamReader);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException(CAPA_COMPONENT_PROPERTIES + " file not found.");
        }
    }
}
