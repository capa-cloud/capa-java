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
package group.rxcloud.capa.infrastructure.env;

import java.util.Objects;

import static group.rxcloud.capa.infrastructure.constants.CapaConstants.Environments.CLOUD_RUNTIMES_ENV_DEPLOY_CLOUD;
import static group.rxcloud.capa.infrastructure.constants.CapaConstants.Environments.CLOUD_RUNTIMES_ENV_DEPLOY_VPC;

/**
 * Capa System Environment Properties.
 */
public abstract class CapaEnvironment {

    /**
     * The cloud deploy environment
     */
    private static DeployCloudEnvironment deployCloudEnvironment;

    /**
     * The vpc deploy environment
     */
    private static DeployVpcEnvironment deployVpcEnvironment;

    static {
        // setup cloud env
        final String cloudRuntimesEnvDeployCloud = System.getProperty(CLOUD_RUNTIMES_ENV_DEPLOY_CLOUD);
        if ("CTRIP".equalsIgnoreCase(cloudRuntimesEnvDeployCloud)) {
            deployCloudEnvironment = DeployCloudEnvironment.CTRIP_IDC;
        }
        if ("AWS".equalsIgnoreCase(cloudRuntimesEnvDeployCloud)) {
            deployCloudEnvironment = DeployCloudEnvironment.AWS_CLOUD;
        }
        if (deployCloudEnvironment == null) {
            deployCloudEnvironment = DeployCloudEnvironment.CTRIP_IDC;
        }

        // setup vpc env
        final String cloudRuntimesEnvDeployVpc = System.getProperty(CLOUD_RUNTIMES_ENV_DEPLOY_VPC);
        if ("FWS".equalsIgnoreCase(cloudRuntimesEnvDeployVpc)
                || "FAT".equalsIgnoreCase(cloudRuntimesEnvDeployVpc)) {
            deployVpcEnvironment = DeployVpcEnvironment.FWS;
        }
        if ("UAT".equalsIgnoreCase(cloudRuntimesEnvDeployVpc)) {
            deployVpcEnvironment = DeployVpcEnvironment.UAT;
        }
        if ("PRO".equalsIgnoreCase(cloudRuntimesEnvDeployVpc)
                || "PROD".equalsIgnoreCase(cloudRuntimesEnvDeployVpc)) {
            deployVpcEnvironment = DeployVpcEnvironment.PRO;
        }
        if (deployVpcEnvironment == null) {
            deployVpcEnvironment = DeployVpcEnvironment.FWS;
        }
    }

    /**
     * The cloud deploy environment
     */
    public enum DeployCloudEnvironment {
        /**
         * e.g.
         * CTRIP
         * AWS
         */
        CTRIP_IDC,
        AWS_CLOUD,
        ;
    }

    /**
     * The vpc deploy environment
     */
    public enum DeployVpcEnvironment {
        /**
         * e.g.
         * FWS
         * UAT
         * PRO
         */
        FWS,
        UAT,
        PRO,
        ;
    }

    // -- setting and getting

    public static DeployCloudEnvironment getDeployCloudEnvironment() {
        return Objects.requireNonNull(deployCloudEnvironment, "Capa Deploy Cloud Environment");
    }

    public static DeployVpcEnvironment getDeployVpcEnvironment() {
        return Objects.requireNonNull(deployVpcEnvironment, "Capa Deploy Vpc Environment");
    }
}
