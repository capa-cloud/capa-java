package group.rxcloud.capa.telemetry;

import group.rxcloud.capa.component.telemetry.CapaTracerConfigLoader;
import group.rxcloud.capa.component.telemetry.SdkTracerBuilder;
import group.rxcloud.capa.component.telemetry.TracerConfig;
import group.rxcloud.capa.infrastructure.config.CapaProperties;

import java.util.Properties;

/**
 * @author: chenyijiang
 * @date: 2021/11/4 10:55
 */
public class CapaTelemetryClientBuilder {

    private final TracerConfig tracerConfig;

    public CapaTelemetryClientBuilder() {
        this(loadTracerConfig());
    }

    public CapaTelemetryClientBuilder(TracerConfig tracerConfig) {
        this.tracerConfig = tracerConfig;
    }

    static TracerConfig loadTracerConfig() {
// load spi capa config store impl
        try {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.get();
            String classPath = properties.getProperty(CapaTracerConfigLoader.class.getName());
            Class<? extends CapaTracerConfigLoader> aClass = (Class<? extends CapaTracerConfigLoader>) Class
                    .forName(classPath);
            return aClass.newInstance().loadTracerConfig();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("No CapaTracerConfigLoader supported.");
        }
    }

    public CapaTelemetryClient build() {
        CapaTelemetryClientGlobal client = new CapaTelemetryClientGlobal();
        client.setTracerProvider(new SdkTracerBuilder(tracerConfig).build());
        // todo others

        return client;
    }
}
