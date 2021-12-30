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

package group.rxcloud.capa.infrastructure.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.rxcloud.capa.infrastructure.CapaProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Load class and create instance from config file.
 * TODO move to {@link CapaProperties}
 */
@Deprecated
public final class SpiUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final Map<String, Constructor> CACHE = new ConcurrentHashMap<>();

    private SpiUtils() {
    }

    public static <T> T loadConfigNullable(String path, Class<T> configType) {
        try (InputStream in = configType.getResourceAsStream(path)) {
            if (in != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
                return OBJECT_MAPPER.readValue(inputStreamReader, configType);
            }
        } catch (IOException e) {
        }
        return null;
    }

    @Nullable
    public static <T> T loadFromSpiComponentFileNullable(Class<T> type, String fileSuffix) {
        return loadFromSpiComponentFileNullable(type, null, null, fileSuffix, false);
    }

    @Nullable
    public static <T> T loadFromSpiComponentFileNullable(Class<T> type, Class[] argTypes, Object[] args,
                                                         String fileSuffix, boolean cache) {
        try {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply(fileSuffix);
            String path = properties.getProperty(type.getName());
            if (path != null) {
                return doNewInstance(path, type, argTypes, args, cache);
            }
            return null;
        } catch (Throwable e) {
        }
        return null;
    }

    @Nullable
    public static <T> T newInstanceWithConstructorCache(String path, Class<T> type) {
        return newInstance(path, type, null, null, true);
    }

    @Nullable
    public static <T> T newInstance(String path, Class<T> type, Class[] argTypes, Object[] args, boolean cache) {
        if (path == null) {
            return null;
        }
        return doNewInstance(path, type, argTypes, args, cache);
    }

    private static String keyOf(String path, Class type, Class[] argTypes) {
        StringBuilder builder = new StringBuilder(type.getName()).append('=').append(path);
        if (argTypes != null) {
            for (Class arg : argTypes) {
                builder.append('_').append(arg.getName());
            }
        }
        return builder.toString();
    }

    @Nonnull
    private static <T> T doNewInstance(String path, Class<T> type, Class[] argTypes, Object[] args, boolean cache) {
        String key = keyOf(path, type, argTypes);
        Constructor<T> targetCons = cache
                ? CACHE.computeIfAbsent(key, k -> findConstructor(path, type, argTypes))
                : findConstructor(path, type, argTypes);
        try {
            return targetCons.getParameterCount() == 0 ? targetCons.newInstance() : targetCons.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Fail to create component. targetType = " + type.getName(), e);
        }
    }

    private static <T> Constructor<T> findConstructor(String path, Class<T> type, Class[] argTypes) {
        try {
            Class<T> aClass = path == null ? type : (Class<T>) Class.forName(path);
            return aClass.getConstructor(argTypes);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Fail to find the constructor. path = " + path, e);
        }
    }
}
