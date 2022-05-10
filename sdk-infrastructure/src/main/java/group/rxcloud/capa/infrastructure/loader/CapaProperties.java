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

import group.rxcloud.vrml.resource.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static group.rxcloud.capa.infrastructure.CapaInfrastructureProperties.Constants.CAPA_COMPONENT_PROPERTIES_PREFIX;
import static group.rxcloud.capa.infrastructure.CapaInfrastructureProperties.Constants.CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX;
import static group.rxcloud.capa.infrastructure.CapaInfrastructureProperties.Constants.CAPA_PROPERTIES_SUFFIX;

/**
 * Global properties for Capa's SDK, using Supplier so they are dynamically resolved.
 */
public abstract class CapaProperties {

    private static final Logger logger = LoggerFactory.getLogger(CapaProperties.class);

    /**
     * Load Capa's component properties.
     *
     * @param componentDomain /capa-component-{componentDomain}.properties
     */
    public static Properties loadComponentProperties(final String componentDomain) {
        final String fileName = CAPA_COMPONENT_PROPERTIES_PREFIX
                + componentDomain.toLowerCase()
                + CAPA_PROPERTIES_SUFFIX;
        return Resources.loadResourcesProperties(fileName)
                .onFailure(throwable -> {
                    if (logger.isErrorEnabled()) {
                        logger.error("[Capa][CapaProperties.loadComponentProperties] fileName[{}] load error!",
                                fileName, throwable);
                    }
                })
                .get();
    }

    /**
     * Load Capa's infrastructure properties.
     *
     * @param infrastructureDomain /capa-infrastructure-{infrastructureDomain}.properties
     */
    public static Properties loadInfrastructureProperties(final String infrastructureDomain) {
        final String fileName = CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX
                + infrastructureDomain.toLowerCase()
                + CAPA_PROPERTIES_SUFFIX;
        return Resources.loadResourcesProperties(fileName)
                .onFailure(throwable -> {
                    if (logger.isErrorEnabled()) {
                        logger.error("[Capa][CapaProperties.loadInfrastructureProperties] fileName[{}] load error!",
                                fileName, throwable);
                    }
                })
                .get();
    }

    /**
     * Load Capa's config file.
     *
     * @param fileName /{fileName}.xxx
     */
    public static <T> T loadConfigFile(final String fileName, final Class<T> fileType) {
        return Resources.loadResources(fileName, fileType)
                .onFailure(throwable -> {
                    if (logger.isErrorEnabled()) {
                        logger.error("[Capa][CapaProperties.loadConfigFile] fileName[{}] fileType[{}] load error!",
                                fileName, fileType.getName(), throwable);
                    }
                })
                .get();
    }
}