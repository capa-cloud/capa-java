package group.rxcloud.capa.spi.log;

import group.rxcloud.capa.component.log.CapaLog4jAppenderAgent;
import group.rxcloud.capa.spi.log.enums.CapaLogLevel;
import group.rxcloud.capa.spi.log.manager.LogManager;
import org.apache.logging.log4j.core.LogEvent;

import java.util.Optional;

public abstract class CapaLog4jAppenderSpi implements CapaLog4jAppenderAgent.CapaLog4jAppender {

    @Override
    public void append(LogEvent event) {
        if (event != null && event.getLevel()!= null) {
            Optional<CapaLogLevel> capaLogLevel = CapaLogLevel.toCapaLogLevel(event.getLevel().name());
            if (capaLogLevel.isPresent() && LogManager.whetherLogsCanOutput(capaLogLevel.get())) {
                this.appendLog(event);
            }
        }
    }

    protected abstract void appendLog(LogEvent event);
}
