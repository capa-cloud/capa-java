package group.rxcloud.capa.component.telemetry.log.manager;

import java.util.Arrays;

public enum CapaLogLevel {
    OFF(1, "OFF"),
    FATAL(2, "FATAL"),
    TRACE(3, "TRACE"),
    DEBUG(4, "DEBUG"),
    INFO(5, "INFO"),
    WARN(6, "WARN"),
    ERROR(7, "ERROR"),
    ALL(8, "ALL");

    final int level;
    final String levelName;


    CapaLogLevel(int level, String levelName) {
        this.level = level;
        this.levelName = levelName;
    }

    public CapaLogLevel toCapaLogLevel(String logLevelArg) {
        return Arrays.stream(CapaLogLevel.values())
                .filter(logLevel -> logLevel.levelName.equalsIgnoreCase(logLevelArg))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Level " + logLevelArg + " is unknown."));
    }
}
