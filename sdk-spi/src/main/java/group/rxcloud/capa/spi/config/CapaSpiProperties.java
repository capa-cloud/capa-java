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
package group.rxcloud.capa.spi.config;

import group.rxcloud.capa.infrastructure.config.CapaProperties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * The Capa SPI environment.
 */
public abstract class CapaSpiProperties {

    /**
     * Gets default options loader.
     *
     * @return the default options loader
     */
    public static CapaSpiOptionsLoader getSpiOptionsLoader() {
        // load spi capa http impl
        try {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.get();
            String classPath = properties.getProperty(CapaSpiOptionsLoader.class.getName());
            Class<? extends CapaSpiOptionsLoader> aClass = (Class<? extends CapaSpiOptionsLoader>) Class.forName(classPath);
            Constructor<? extends CapaSpiOptionsLoader> constructor = aClass.getConstructor();
            Object newInstance = constructor.newInstance();
            return (CapaSpiOptionsLoader) newInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("No CapaSpiOptionsLoader implement supported.");
        }
    }
}
