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
package group.rxcloud.capa.infrastructure.plugin;

import group.rxcloud.capa.infrastructure.CapaClassLoader;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * The Plugin SPI loader.
 */
public final class PluginLoader {

    private static final Map<Class, Object> pluginImplCache;

    static {
        pluginImplCache = new ConcurrentHashMap<>();
    }

    public static Map<Class, Object> getPluginImplCache() {
        return pluginImplCache;
    }

    /**
     * Load plugin impl by java spi.
     *
     * @param <T>              the plugin interface type
     * @param pluginSuperClazz the plugin interface class
     * @return the optional of plugin impl
     */
    public static <T> Optional<T> loadPluginImpl(Class<T> pluginSuperClazz) {
        Object o = pluginImplCache.computeIfAbsent(pluginSuperClazz,
                aClass -> CapaClassLoader.loadPluginClassObj(pluginSuperClazz));
        return Optional.ofNullable((T) o);
    }

    /**
     * Load plugin impl by java spi.
     *
     * @param <T>              the plugin interface type
     * @param pluginSuperClazz the plugin interface class
     * @param defaultPluginObj the default plugin obj
     * @return the plugin impl
     */
    public static <T> T loadPluginImpl(Class<T> pluginSuperClazz, Supplier<T> defaultPluginObj) {
        Object o = pluginImplCache.computeIfAbsent(pluginSuperClazz,
                aClass -> {
                    T pluginClassObj = CapaClassLoader.loadPluginClassObj(pluginSuperClazz);
                    if (pluginClassObj != null) {
                        return pluginClassObj;
                    }
                    return defaultPluginObj.get();
                });
        return (T) o;
    }
}
