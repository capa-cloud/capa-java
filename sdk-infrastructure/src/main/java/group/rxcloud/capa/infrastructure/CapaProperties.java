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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Function2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static group.rxcloud.capa.infrastructure.CapaConstants.Properties.CAPA_COMPONENT_PROPERTIES_PREFIX;
import static group.rxcloud.capa.infrastructure.CapaConstants.Properties.CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX;
import static group.rxcloud.capa.infrastructure.CapaConstants.Properties.CAPA_PROPERTIES_SUFFIX;
import static group.rxcloud.capa.infrastructure.InnerModule.FILE_CACHE_MAP;
import static group.rxcloud.capa.infrastructure.InnerModule.loadCapaConfig;
import static group.rxcloud.capa.infrastructure.InnerModule.loadCapaFileByJavaSpi;
import static group.rxcloud.capa.infrastructure.InnerModule.loadCapaProperties;

/**
 * Global properties for Capa's SDK, using Supplier so they are dynamically resolved.
 */
public abstract class CapaProperties {

    /**
     * Capa's component properties supplier.
     */
    public static final Function<String, Properties> COMPONENT_PROPERTIES_SUPPLIER
            = (componentDomain) -> (Properties) FILE_CACHE_MAP.computeIfAbsent(componentDomain,
            s -> {
                final String fileName = CAPA_COMPONENT_PROPERTIES_PREFIX
                        + componentDomain.toLowerCase()
                        + CAPA_PROPERTIES_SUFFIX;
                return loadCapaProperties(fileName);
            });

    /**
     * Capa's infrastructure properties supplier.
     */
    public static final Function<String, Properties> INFRASTRUCTURE_PROPERTIES_SUPPLIER
            = (infrastructureDomain) -> (Properties) FILE_CACHE_MAP.computeIfAbsent(infrastructureDomain,
            s -> {
                final String fileName = CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX
                        + infrastructureDomain.toLowerCase()
                        + CAPA_PROPERTIES_SUFFIX;
                return loadCapaProperties(fileName);
            });

    /**
     * Capa's plugin properties supplier.
     */
    public static final Function<Class, Object> PLUGIN_PROPERTIES_SUPPLIER
            = (clazz) -> FILE_CACHE_MAP.computeIfAbsent(clazz.getName(),
            s -> loadCapaFileByJavaSpi(clazz));

    /**
     * Capa's config file supplier.
     */
    public static final Function2<String, Class, Object> CONFIG_FILE_SUPPLIER
            = (fileName, clazz) -> FILE_CACHE_MAP.computeIfAbsent(fileName,
            s -> loadCapaConfig(s, clazz));
}

interface InnerModule {

    /**
     * Capa's file cache map.
     */
    Map<String, Object> FILE_CACHE_MAP = new ConcurrentHashMap<>();

    ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    static Properties loadCapaProperties(final String fileName) {
        Objects.requireNonNull(fileName, "fileName not found.");
        try (InputStream in = CapaProperties.class.getResourceAsStream(fileName)) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(inputStreamReader);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException(fileName + " file not found.", e);
        }
    }

    static <T> T loadCapaConfig(final String fileName, Class<T> configClazz) {
        Objects.requireNonNull(fileName, "fileName not found.");
        try (InputStream in = CapaProperties.class.getResourceAsStream(fileName)) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            return OBJECT_MAPPER.readValue(inputStreamReader, configClazz);
        } catch (JsonParseException | JsonMappingException e) {
            throw new IllegalArgumentException(fileName + " file not load.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException(fileName + " file not found.", e);
        }
    }

    static <T> T loadCapaFileByJavaSpi(Class<T> configClazz) {
        try {
            ServiceLoader<T> loader = ServiceLoader.load(configClazz);
            Iterator<T> iterator = loader.iterator();
            if (!iterator.hasNext()) {
                return null;
            }
            return iterator.next();
        } catch (Exception e) {
            throw new IllegalArgumentException(configClazz.getName() + " spi file not found.", e);
        }
    }
}
