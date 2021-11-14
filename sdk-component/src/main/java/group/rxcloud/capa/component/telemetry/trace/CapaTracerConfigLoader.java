package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.SpiUtils;
import group.rxcloud.capa.component.telemetry.trace.config.TracerConfig;

/**
 * @author: chenyijiang
 * @date: 2021/11/10 12:45
 */
public interface CapaTracerConfigLoader {

    CapaTracerConfigLoader DEFAULT = new CapaTracerConfigLoader() {
    };

    String FILE_PATH = "/capa-tracer.json";

    default TracerConfig loadTracerConfig() {
        return SpiUtils.loadConfigNullable(FILE_PATH, TracerConfig.class);
    }

}
