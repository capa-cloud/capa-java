package group.rxcloud.capa.component.telemetry;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author: chenyijiang
 * @date: 2021/11/3 17:14
 */
public class SamplerConfig implements Serializable {

    private static final long serialVersionUID = -2113523925814197551L;

    // 默认全部取样
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig();

    private String name = "_DEFAULT_SAMPLER";

    private double sampleRatio = 1.0;

    // by time
    private long intervalMillis = 1L;

    private long minSampleCount = 1;

    private long maxSampleCount = Long.MAX_VALUE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMinSampleCount() {
        return minSampleCount;
    }

    public void setMinSampleCount(long minSampleCount) {
        this.minSampleCount = minSampleCount;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    public void setIntervalMillis(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public void setInterval(long interval, TimeUnit timeUnit) {
        this.intervalMillis = timeUnit.toMillis(interval);
    }

    public long getMaxSampleCount() {
        return maxSampleCount;
    }

    public void setMaxSampleCount(long maxSampleCount) {
        this.maxSampleCount = maxSampleCount;
    }

    public double getSampleRatio() {
        return sampleRatio;
    }

    public void setSampleRatio(double sampleRatio) {
        this.sampleRatio = sampleRatio;
    }
}
