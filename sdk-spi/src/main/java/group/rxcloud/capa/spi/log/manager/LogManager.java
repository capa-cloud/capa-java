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
package group.rxcloud.capa.spi.log.manager;

import group.rxcloud.capa.spi.log.configuration.LogConfiguration;
import group.rxcloud.capa.spi.log.enums.CapaLogLevel;

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
     * If there is no corresponding configuration information or the switch of dynamic configuration log level is set to false,
     * then only logs of info level and above will be output.
     * <p>
     * If the value of the dynamic configuration log level switch is true, only the log with the log level configured as true will be output.
     * <p>
     * If the log level is not in the {@link CapaLogLevel#values()}, the log is not output.
     *
     * @return If the log can be output, it is true, otherwise it is false.
     */
    public static Boolean whetherLogsCanOutput(CapaLogLevel capaLogLevel) {
        if (!LogConfiguration.containsKey(LOG_LEVEL_SWITCH_NAME) || Boolean.FALSE.equals(LogConfiguration.get(LOG_LEVEL_SWITCH_NAME))) {
            return isLogsLevelClosedWithDefault(capaLogLevel);
        }
        switch (capaLogLevel) {
            case ALL:
                return !LogConfiguration.containsKey(ALL_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(ALL_LEVEL_SWITCH_NAME));
            case TRACE:
                return !LogConfiguration.containsKey(TRACE_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(TRACE_LEVEL_SWITCH_NAME));
            case DEBUG:
                return !LogConfiguration.containsKey(DEBUG_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(DEBUG_LEVEL_SWITCH_NAME));
            case INFO:
                return !LogConfiguration.containsKey(INFO_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(INFO_LEVEL_SWITCH_NAME));
            case WARN:
                return !LogConfiguration.containsKey(WARN_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(WARN_LEVEL_SWITCH_NAME));
            case ERROR:
                return !LogConfiguration.containsKey(ERROR_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(ERROR_LEVEL_SWITCH_NAME));
            case FATAL:
                return !LogConfiguration.containsKey(FATAL_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(FATAL_LEVEL_SWITCH_NAME));
            case OFF:
                return !LogConfiguration.containsKey(OFF_LEVEL_SWITCH_NAME)
                        ? Boolean.FALSE
                        : Boolean.parseBoolean(LogConfiguration.get(OFF_LEVEL_SWITCH_NAME));
            default:
                return Boolean.FALSE;
        }
    }

    /**
     * Logs of the {@link CapaLogLevel#INFO} level or higher are normally output, but logs of the {@link CapaLogLevel#INFO} level lower are not output.
     *
     * @return whether the capaLogLevel's priority is equal or more then {@link CapaLogLevel#INFO}.
     */
    private static Boolean isLogsLevelClosedWithDefault(CapaLogLevel capaLogLevel) {
        return capaLogLevel.getLevel() >= CapaLogLevel.INFO.getLevel();
    }
}
