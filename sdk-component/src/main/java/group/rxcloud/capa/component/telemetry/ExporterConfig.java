package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:06
 */
public class ExporterConfig implements Serializable {

    private static final long serialVersionUID = 6564148803113465022L;

    private String exporterClass;

    private Object[] constructArgs;

    private transient SpanExporter exporterInstance;

    private boolean useBatch;

    private Integer maxExportBatchSize;

    private Integer maxQueueSize;

    private Long scheduleDelayMillis;

    private Long exporterTimeoutMillis;

    public SpanExporter getExporterInstance() {
        if (exporterInstance == null) {
            try {
                Class<? extends SpanExporter> aClass = (Class<? extends SpanExporter>) Class
                        .forName(exporterClass.trim());
                if (constructArgs == null || constructArgs.length == 0) {
                    exporterInstance = aClass.getConstructor().newInstance();
                } else {
                    // find the first match constructor.
                    for (Constructor<?> constructor : aClass.getDeclaredConstructors()) {
                        if (constructor.getParameterCount() == constructArgs.length) {
                            boolean match = true;
                            for (int i = 0; i < constructArgs.length; i++) {
                                if (!isSameType(constructor.getParameterTypes()[i], constructArgs[i])) {
                                    match = false;
                                    break;
                                }
                            }
                            if (match) {
                                exporterInstance = (SpanExporter) constructor.newInstance(constructArgs);
                            }
                        }
                    }
                }

            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("No Capa Span Exporter supported.", e);
            }
        }
        return exporterInstance;
    }

    public void setExporterInstance(SpanExporter exporterInstance) {
        this.exporterInstance = exporterInstance;
    }

    private boolean isSameType(Class type, Object arg) {
        if (arg == null) {
            return !type.isPrimitive();
        }

        if (type.isInstance(arg)) {
            return true;
        }

        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return arg instanceof Boolean;
            }
            if (type == short.class) {
                return arg instanceof Short;
            }
            if (type == int.class) {
                return arg instanceof Integer;
            }
            if (type == long.class) {
                return arg instanceof Long;
            }
            if (type == float.class) {
                return arg instanceof Float;
            }
            if (type == double.class) {
                return arg instanceof Double;
            }
            if (type == byte.class) {
                return arg instanceof Byte;
            }
            if (type == char.class) {
                return arg instanceof Character;
            }
        }

        return false;
    }

    public String getExporterClass() {
        return exporterClass;
    }

    public void setExporterClass(String exporterClass) {
        this.exporterClass = exporterClass;
    }

    public Object[] getConstructArgs() {
        return constructArgs;
    }

    public void setConstructArgs(Object... constructArgs) {
        this.constructArgs = constructArgs;
    }

    public boolean isUseBatch() {
        return useBatch;
    }

    public void setUseBatch(boolean useBatch) {
        this.useBatch = useBatch;
    }

    public Integer getMaxExportBatchSize() {
        return maxExportBatchSize;
    }

    public void setMaxExportBatchSize(Integer maxExportBatchSize) {
        this.maxExportBatchSize = maxExportBatchSize;
    }

    public Integer getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(Integer maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public Long getScheduleDelayMillis() {
        return scheduleDelayMillis;
    }

    public void setScheduleDelayMillis(Long scheduleDelayMillis) {
        this.scheduleDelayMillis = scheduleDelayMillis;
    }

    public void setScheduleDelay(Long scheduleDelay, TimeUnit timeUnit) {
        this.scheduleDelayMillis = timeUnit.toMillis(scheduleDelay);
    }

    public Long getExporterTimeoutMillis() {
        return exporterTimeoutMillis;
    }

    public void setExporterTimeoutMillis(Long exporterTimeoutMillis) {
        this.exporterTimeoutMillis = exporterTimeoutMillis;
    }

    public void setExporterTimeout(Long exporterTimeout, TimeUnit timeUnit) {
        this.exporterTimeoutMillis = timeUnit.toMillis(exporterTimeout);
    }
}
