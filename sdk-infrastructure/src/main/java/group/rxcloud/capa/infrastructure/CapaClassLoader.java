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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * The Capa SPI Class loader.
 */
public final class CapaClassLoader {

    /**
     * Load component class obj.
     *
     * @param <T>             the target class type
     * @param componentDomain the component domain
     * @param superClazz      the interface class type
     * @return the target spi class obj
     */
    public static <T> T loadComponentClassObj(String componentDomain, Class<T> superClazz) {
        return loadComponentClassObj(componentDomain, superClazz, null, null);
    }

    /**
     * Load component class obj.
     *
     * @param <T>             the target class type
     * @param componentDomain the component domain
     * @param superClazz      the interface class type
     * @param parameterTypes  the constructor parameters
     * @param initargs        the constructor initargs
     * @return the target spi class obj
     */
    public static <T> T loadComponentClassObj(String componentDomain, Class<T> superClazz, Class<?>[] parameterTypes, Object[] initargs) {
        Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply(componentDomain);
        String implClassPath = properties.getProperty(superClazz.getName());
        return loadClassObj(implClassPath, parameterTypes, initargs);
    }

    /**
     * Load infrastructure class obj.
     *
     * @param <T>                  the target class type
     * @param infrastructureDomain the infrastructure domain
     * @param superClazz           the interface class type
     * @return the target spi class obj
     */
    public static <T> T loadInfrastructureClassObj(String infrastructureDomain, Class<T> superClazz) {
        return loadInfrastructureClassObj(infrastructureDomain, superClazz, null, null);
    }

    /**
     * Load infrastructure class obj.
     *
     * @param <T>                  the target class type
     * @param infrastructureDomain the infrastructure domain
     * @param superClazz           the interface class type
     * @param parameterTypes       the constructor parameters
     * @param initargs             the constructor initargs
     * @return the target spi class obj
     */
    public static <T> T loadInfrastructureClassObj(String infrastructureDomain, Class<T> superClazz, Class<?>[] parameterTypes, Object[] initargs) {
        Properties properties = CapaProperties.INFRASTRUCTURE_PROPERTIES_SUPPLIER.apply(infrastructureDomain);
        String implClassPath = properties.getProperty(superClazz.getName());
        return loadClassObj(implClassPath, parameterTypes, initargs);
    }

    private static <T> T loadClassObj(String classPath, Class<?>[] parameterTypes, Object[] initargs) {
        try {
            Class<T> aClass = (Class<T>) Class.forName(classPath);
            Constructor<T> constructor = aClass.getConstructor(parameterTypes);
            Object newInstance = constructor.newInstance(initargs);
            return (T) newInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Capa load class error: [" + classPath + "] not found.");
        }
    }
}
