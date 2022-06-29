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
package group.rxcloud.capa.infrastructure.loader;

import group.rxcloud.vrml.core.serialization.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * The Capa SPI Class obj loader.
 */
public final class CapaClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(CapaClassLoader.class);

    /**
     * Load component class obj.
     *
     * @param <T>             the target class type
     * @param componentDomain the component domain
     * @param clazz           the interface class type
     * @return the target spi class obj
     */
    public static <T> T loadComponentClassObj(String componentDomain, Class<T> clazz) {
        return loadComponentClassObj(componentDomain, clazz, null, null);
    }

    /**
     * Load component class obj.
     *
     * @param <T>             the target class type
     * @param componentDomain the component domain
     * @param clazz           the interface class type
     * @param parameterTypes  the constructor parameters
     * @param initargs        the constructor initargs
     * @return the target spi class obj
     */
    public static <T> T loadComponentClassObj(String componentDomain, Class<T> clazz, Class<?>[] parameterTypes, Object[] initargs) {
        Properties properties = CapaProperties.loadComponentProperties(componentDomain);
        String implClassPath = properties.getProperty(clazz.getName());
        return loadClassObj(implClassPath, parameterTypes, initargs);
    }

    /**
     * Load infrastructure class obj.
     *
     * @param <T>                  the target class type
     * @param infrastructureDomain the infrastructure domain
     * @param clazz                the interface class type
     * @return the target spi class obj
     */
    public static <T> T loadInfrastructureClassObj(String infrastructureDomain, Class<T> clazz) {
        return loadInfrastructureClassObj(infrastructureDomain, clazz, null, null);
    }

    /**
     * Load infrastructure class obj.
     *
     * @param <T>                  the target class type
     * @param infrastructureDomain the infrastructure domain
     * @param clazz                the interface class type
     * @param parameterTypes       the constructor parameters
     * @param initargs             the constructor initargs
     * @return the target spi class obj
     */
    public static <T> T loadInfrastructureClassObj(String infrastructureDomain, Class<T> clazz, Class<?>[] parameterTypes, Object[] initargs) {
        Properties properties = CapaProperties.loadInfrastructureProperties(infrastructureDomain);
        String implClassPath = properties.getProperty(clazz.getName());
        return loadClassObj(implClassPath, parameterTypes, initargs);
    }

    // -- Class loader

    private static <T> T loadClassObj(String classPath, Class<?>[] parameterTypes, Object[] initargs) {
        Throwable throwable = null;
        try {
            Class<T> aClass = (Class<T>) Class.forName(classPath);
            Constructor<T> constructor = aClass.getConstructor(parameterTypes);
            Object newInstance = constructor.newInstance(initargs);
            return (T) newInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throwable = e;
            throw new IllegalArgumentException("Capa load class[" + classPath + "] not found error: " + e.getMessage());
        } finally {
            if (throwable != null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("[Capa][CapaClassLoader.loadClassObj] classPath[{}] parameterTypes[{}] initargs[{}] load error!",
                            classPath, Serialization.GSON.toJson(parameterTypes), Serialization.GSON.toJson(initargs), throwable);
                }
            }
        }
    }
}
