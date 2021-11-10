package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.sdk.trace.IdGenerator;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: chenyijiang
 * @date: 2021/11/4 11:44
 */
public class TracerConfig implements Serializable {

    private static final long serialVersionUID = 6587103489345563395L;

    private String idGenerator;

    private transient IdGenerator idGeneratorInstance;

    private SamplerConfig sampler;

    private SpanLimitsConfig spanLimits;

    private List<ExporterConfig> exporters = new ArrayList<>();

    public String getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(String className) {
        this.idGenerator = className;
    }

    public IdGenerator getIdGeneratorInstance() {
        // try load id generator if configured.
        if (idGeneratorInstance == null && idGenerator != null) {
            try {
                Class<? extends IdGenerator> aClass = (Class<? extends IdGenerator>) Class
                        .forName(idGenerator.trim());
                Constructor<? extends IdGenerator> constructor = aClass.getConstructor();
                idGeneratorInstance = constructor.newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("No Trace Id Generator supported. name=" + idGenerator + ". ", e);
            }
        }
        return idGeneratorInstance;
    }

    public void setIdGeneratorInstance(IdGenerator idGeneratorInstance) {
        this.idGeneratorInstance = idGeneratorInstance;
    }

    public SamplerConfig getSampler() {
        return sampler;
    }

    public void setSampler(SamplerConfig sampler) {
        this.sampler = sampler;
    }

    public SpanLimitsConfig getSpanLimits() {
        return spanLimits;
    }

    public void setSpanLimits(SpanLimitsConfig spanLimits) {
        this.spanLimits = spanLimits;
    }

    public List<ExporterConfig> getExporters() {
        return Collections.unmodifiableList(exporters);
    }

    public void setExporters(List<ExporterConfig> exporters) {
        this.exporters = new ArrayList<>(exporters);
    }

    public void addExporter(ExporterConfig exporter) {
        exporters.add(exporter);
    }
}
