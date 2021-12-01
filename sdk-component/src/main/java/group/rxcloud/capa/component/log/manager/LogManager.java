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
package group.rxcloud.capa.component.log.manager;

import group.rxcloud.capa.component.log.configuration.LogSwitchConfiguration;
import group.rxcloud.capa.component.log.enums.CapaLogLevel;

import java.util.Map;

/**
 * LogManager, to manage log output levels.
 */
public class LogManager {

    /**
     * Dynamically adjust the log level switch name.
     */
    private static final String LOG_LEVEL_SWITCH_NAME = "logLevelSwitch";
    /**
     * All level switch name.
     */
    private static final String ALL_LEVEL_SWITCH_NAME = "allLevelSwitch";
    /**
     * Trace level switch name.
     */
    private static final String TRACE_LEVEL_SWITCH_NAME = "traceLevelSwitch";
    /**
     * Debug level switch name.
     */
    private static final String DEBUG_LEVEL_SWITCH_NAME = "debugLevelSwitch";
    /**
     * Info level switch name.
     */
    private static final String INFO_LEVEL_SWITCH_NAME = "infoLevelSwitch";
    /**
     * Warn level switch.
     */
    private static final String WARN_LEVEL_SWITCH_NAME = "warnLevelSwitch";
    /**
     * Error level switch.
     */
    private static final String ERROR_LEVEL_SWITCH_NAME = "errorLevelSwitch";
    /**
     * Fatal level switch
     */
    private static final String FATAL_LEVEL_SWITCH_NAME = "fatalLevelSwitch";
    /**
     * Off level switch.
     */
    private static final String OFF_LEVEL_SWITCH_NAME = "offLevelSwitch";

    /**
     * Whether logs can be output.
     * @param capaLogLevel
     * @return
     */
    public static Boolean whetherLogsCanBeOutput(CapaLogLevel capaLogLevel) {
        Map<String, Boolean> logsSwitchConfigs = LogSwitchConfiguration.getLogsSwitchConfig();
        //no configuration instance or logs switch configuration, then follow the default logic.
        if (logsSwitchConfigs == null
                || logsSwitchConfigs.isEmpty()
                || Boolean.FALSE.equals(logsSwitchConfigs.get(LOG_LEVEL_SWITCH_NAME))) {
            return isLogsLevelClosedWithDefault(capaLogLevel);
        }

        switch (capaLogLevel) {
            case ALL:
                return logsSwitchConfigs.get(ALL_LEVEL_SWITCH_NAME) == null
                        ? Boolean.FALSE
                        : logsSwitchConfigs.get(ALL_LEVEL_SWITCH_NAME);
            case TRACE:
                return logsSwitchConfigs.get(TRACE_LEVEL_SWITCH_NAME) == null
                        ? Boolean.FALSE
                        : logsSwitchConfigs.get(TRACE_LEVEL_SWITCH_NAME);
            case DEBUG:
                return logsSwitchConfigs.get(DEBUG_LEVEL_SWITCH_NAME) == null
                        ? Boolean.FALSE
                        : logsSwitchConfigs.get(DEBUG_LEVEL_SWITCH_NAME);
            case INFO:
                return logsSwitchConfigs.get(INFO_LEVEL_SWITCH_NAME) == null
                        ? Boolean.TRUE
                        : logsSwitchConfigs.get(INFO_LEVEL_SWITCH_NAME);
            case WARN:
                return logsSwitchConfigs.get(WARN_LEVEL_SWITCH_NAME) == null
                        ? Boolean.TRUE
                        : logsSwitchConfigs.get(WARN_LEVEL_SWITCH_NAME);
            case ERROR:
                return logsSwitchConfigs.get(ERROR_LEVEL_SWITCH_NAME) == null
                        ? Boolean.TRUE
                        : logsSwitchConfigs.get(ERROR_LEVEL_SWITCH_NAME);
            case FATAL:
                return logsSwitchConfigs.get(FATAL_LEVEL_SWITCH_NAME) == null
                        ? Boolean.TRUE
                        : logsSwitchConfigs.get(FATAL_LEVEL_SWITCH_NAME);
            case OFF:
                return logsSwitchConfigs.get(OFF_LEVEL_SWITCH_NAME) == null
                        ? Boolean.TRUE
                        : logsSwitchConfigs.get(OFF_LEVEL_SWITCH_NAME) == null;
            default:
                return Boolean.FALSE;
        }
    }

    /**
     * Logs of the {@link CapaLogLevel#INFO} level or higher are normally output, but logs of the {@link CapaLogLevel#INFO} level lower are not output.
     *
     * @param capaLogLevel
     * @return whether the capaLogLevel's priority is equal or more then {@link CapaLogLevel#INFO}.
     */
    private static Boolean isLogsLevelClosedWithDefault(CapaLogLevel capaLogLevel) {
        return capaLogLevel.getLevel() >= CapaLogLevel.INFO.getLevel();
    }
}
