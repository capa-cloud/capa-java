package group.rxcloud.capa.component.telemetry.trace.config;

import java.io.Serializable;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:14
 */
public class SamplerConfig implements Serializable {

    private static final long serialVersionUID = -2113523925814197551L;

    // 默认全部取样
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig();

    private String name = "_DEFAULT_SAMPLER";

    private boolean disable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
