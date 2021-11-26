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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import static group.rxcloud.capa.infrastructure.CapaConstants.Properties.CAPA_COMPONENT_PROPERTIES_PREFIX;
import static group.rxcloud.capa.infrastructure.CapaConstants.Properties.CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX;
import static group.rxcloud.capa.infrastructure.CapaConstants.Properties.CAPA_PROPERTIES_SUFFIX;

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
    public static final Supplier<Integer> HTTP_CLIENT_READ_TIMEOUT_SECONDS
            = () -> DEFAULT_HTTP_CLIENT_READTIMEOUTSECONDS;

    /**
     * Capa's properties cache map.
     */
    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<>();

    /**
     * Capa's infrastructure properties.
     */
    public static final Function<String, Properties> INFRASTRUCTURE_PROPERTIES_SUPPLIER
            = (infrastructureDomain) -> PROPERTIES_MAP.computeIfAbsent(infrastructureDomain,
            s -> {
                final String fileName = CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX
                        + infrastructureDomain.toLowerCase()
                        + CAPA_PROPERTIES_SUFFIX;
                return loadCapaProperties(fileName);
            });

    /**
     * Capa's component properties.
     */
    public static final Function<String, Properties> COMPONENT_PROPERTIES_SUPPLIER
            = (componentDomain) -> PROPERTIES_MAP.computeIfAbsent(componentDomain,
            s -> {
                final String fileName = CAPA_COMPONENT_PROPERTIES_PREFIX
                        + componentDomain.toLowerCase()
                        + CAPA_PROPERTIES_SUFFIX;
                return loadCapaProperties(fileName);
            });

    private static Properties loadCapaProperties(final String fileName) {
        Objects.requireNonNull(fileName, "fileName not found.");
        try (InputStream in = CapaProperties.class.getResourceAsStream(fileName)) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(inputStreamReader);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException(fileName + " file not found.");
        }
    }
}
