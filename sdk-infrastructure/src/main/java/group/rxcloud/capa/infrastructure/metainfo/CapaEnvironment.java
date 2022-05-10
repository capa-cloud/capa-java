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
package group.rxcloud.capa.infrastructure.metainfo;

/**
 * Capa System Environment Properties.
 */
public interface CapaEnvironment {

    /**
     * Gets deploy cloud, like:
     * <pre>
     * + AWS
     * + ALIBABA
     * </pre>
     *
     * @return the deploy cloud name
     */
    String getDeployCloud();

    /**
     * Gets deploy region, like:
     * <pre>
     * + SINGAPORE
     * + TOKYO
     * + SHANGHAI
     * </pre>
     *
     * @return the deploy region name
     */
    String getDeployRegion();

    /**
     * Gets deploy env, like:
     * <pre>
     * + FAT
     * + UAT
     * + PRO
     * </pre>
     *
     * @return the deploy env name
     */
    String getDeployEnv();
}
