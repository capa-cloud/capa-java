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

public class LogManager {

    public static Boolean isLogLevelSwitchOn(CapaLogLevel capaLogLevel) {
        LogSwitchConfiguration.LogsSwitchConfig logsSwitchConfig = LogSwitchConfiguration.getLogsSwitchConfig();
        //no configuration instance or logs switch configuration, then follow the default logic
        if (logsSwitchConfig == null || Boolean.FALSE.equals(logsSwitchConfig.getLogsSwitch())) {
            return isLogsLevelClosedWithDefault(capaLogLevel);
        }

        switch (capaLogLevel) {
            case ALL:
                return logsSwitchConfig.getAllLevelSwitch() == null ? Boolean.FALSE : logsSwitchConfig.getAllLevelSwitch();
            case TRACE:
                return logsSwitchConfig.getTraceLevelSwitch() == null ? Boolean.FALSE : logsSwitchConfig.getTraceLevelSwitch();
            case DEBUG:
                return logsSwitchConfig.getDebugLevelSwitch() == null ? Boolean.FALSE : logsSwitchConfig.getDebugLevelSwitch();
            case INFO:
                return logsSwitchConfig.getInfoLevelSwitch() == null ? Boolean.TRUE : logsSwitchConfig.getInfoLevelSwitch();
            case WARN:
                return logsSwitchConfig.getWarnLevelSwitch() == null ? Boolean.TRUE : logsSwitchConfig.getWarnLevelSwitch();
            case ERROR:
                return logsSwitchConfig.getErrorLevelSwitch() == null ? Boolean.TRUE : logsSwitchConfig.getErrorLevelSwitch();
            case FATAL:
                return logsSwitchConfig.getFatalLevelSwitch() == null ? Boolean.TRUE : logsSwitchConfig.getFatalLevelSwitch();
            case OFF:
                return logsSwitchConfig.getOffLevelSwitch() == null ? Boolean.TRUE : logsSwitchConfig.getOffLevelSwitch();
            default:
                return Boolean.FALSE;
        }
    }

    /**
     * Logs of the {@link CapaLogLevel#INFO} level or higher are normally output, but logs of the {@link CapaLogLevel#INFO} level lower are not output.
     *
     * @param capaLogLevel
     * @return whether capaLogLevel's priority is equal or more then {@link CapaLogLevel#INFO}.
     */
    private static Boolean isLogsLevelClosedWithDefault(CapaLogLevel capaLogLevel) {
        return capaLogLevel.getLevel() >= CapaLogLevel.INFO.getLevel();
    }
}
