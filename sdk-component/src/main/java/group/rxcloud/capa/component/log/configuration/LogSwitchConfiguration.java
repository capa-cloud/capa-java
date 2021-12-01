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
package group.rxcloud.capa.component.log.configuration;

import com.google.common.collect.Lists;
import group.rxcloud.capa.infrastructure.hook.ConfigurationHooks;
import group.rxcloud.capa.infrastructure.hook.Mixer;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

import java.util.Optional;

public class LogSwitchConfiguration {

    private static final Optional<ConfigurationHooks> configurationHooks;
    private static final String LOGS_SWITCH_CONFIG_FILE_NAME = "logs-switch.properties";
    private static LogsSwitchConfig logsSwitchConfig;

    static {
        configurationHooks = Mixer.configurationHooksNullable();
        configurationHooks.ifPresent(configurationHooks -> {
            subscribeConfiguration(configurationHooks);
        });
    }

    private static void subscribeConfiguration(ConfigurationHooks configurationHooks) {
        // todo get storeName and appId from hooks of configuration
        String storeName = "";
        String appId = "";
        Flux<SubConfigurationResp<LogsSwitchConfig>> configFlux = configurationHooks.subscribeConfiguration(
                storeName,
                appId,
                Lists.newArrayList(LOGS_SWITCH_CONFIG_FILE_NAME),
                null,
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                TypeRef.get(LogsSwitchConfig.class));
        configFlux.subscribe(resp -> {
            if (CollectionUtils.isNotEmpty(resp.getItems())) {
                logsSwitchConfig = resp.getItems().get(0).getContent();
            }
        });
    }

    public static LogsSwitchConfig getLogsSwitchConfig() {
        return logsSwitchConfig;
    }

    public static class LogsSwitchConfig {
        private Boolean logsSwitch;
        private Boolean allLevelSwitch;
        private Boolean traceLevelSwitch;
        private Boolean debugLevelSwitch;
        private Boolean infoLevelSwitch;
        private Boolean warnLevelSwitch;
        private Boolean errorLevelSwitch;
        private Boolean fatalLevelSwitch;
        private Boolean offLevelSwitch;

        public Boolean getLogsSwitch() {
            return logsSwitch;
        }

        public void setLogsSwitch(Boolean logsSwitch) {
            this.logsSwitch = logsSwitch;
        }

        public Boolean getAllLevelSwitch() {
            return allLevelSwitch;
        }

        public void setAllLevelSwitch(Boolean allLevelSwitch) {
            this.allLevelSwitch = allLevelSwitch;
        }

        public Boolean getTraceLevelSwitch() {
            return traceLevelSwitch;
        }

        public void setTraceLevelSwitch(Boolean traceLevelSwitch) {
            this.traceLevelSwitch = traceLevelSwitch;
        }

        public Boolean getDebugLevelSwitch() {
            return debugLevelSwitch;
        }

        public void setDebugLevelSwitch(Boolean debugLevelSwitch) {
            this.debugLevelSwitch = debugLevelSwitch;
        }

        public Boolean getInfoLevelSwitch() {
            return infoLevelSwitch;
        }

        public void setInfoLevelSwitch(Boolean infoLevelSwitch) {
            this.infoLevelSwitch = infoLevelSwitch;
        }

        public Boolean getWarnLevelSwitch() {
            return warnLevelSwitch;
        }

        public void setWarnLevelSwitch(Boolean warnLevelSwitch) {
            this.warnLevelSwitch = warnLevelSwitch;
        }

        public Boolean getErrorLevelSwitch() {
            return errorLevelSwitch;
        }

        public void setErrorLevelSwitch(Boolean errorLevelSwitch) {
            this.errorLevelSwitch = errorLevelSwitch;
        }

        public Boolean getFatalLevelSwitch() {
            return fatalLevelSwitch;
        }

        public void setFatalLevelSwitch(Boolean fatalLevelSwitch) {
            this.fatalLevelSwitch = fatalLevelSwitch;
        }

        public Boolean getOffLevelSwitch() {
            return offLevelSwitch;
        }

        public void setOffLevelSwitch(Boolean offLevelSwitch) {
            this.offLevelSwitch = offLevelSwitch;
        }
    }
}
