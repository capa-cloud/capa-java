package group.rxcloud.capa.spi.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import group.rxcloud.capa.component.log.CapaLogbackAppenderAgent;
import group.rxcloud.capa.spi.log.enums.CapaLogLevel;
import group.rxcloud.capa.spi.log.manager.LogManager;

import java.util.Optional;

public abstract class CapaLogbackAppenderSpi extends UnsynchronizedAppenderBase<ILoggingEvent>
        implements CapaLogbackAppenderAgent.CapaLogbackAppender {

    @Override
    public void append(ILoggingEvent event) {
        if (event != null && event.getLevel() != null) {
            Optional<CapaLogLevel> capaLogLevel = CapaLogLevel.toCapaLogLevel(event.getLevel().levelStr);
            if (capaLogLevel.isPresent() && LogManager.whetherLogsCanBeOutput(capaLogLevel.get())) {
                this.append(event);
            }
        }
    }

    protected abstract void appendLog(ILoggingEvent event);
}
