package group.rxcloud.capa.component.telemetry.metrics;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import group.rxcloud.capa.component.telemetry.trace.CapaTraceSampler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 17:16
 */
public class CapaMetricsSamplerTest {

    @Test
    public void getInstance() {
        assertNotNull(CapaMetricsSampler.getInstance());
        CapaMetricsSampler.getInstance().update(SamplerConfig.DEFAULT_CONFIG);
        assertTrue(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1L, null, null));
        assertTrue(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1.0, null, null));

        CapaMetricsSampler.getInstance().update(null);
        assertTrue(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1L, null, null));
        assertTrue(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1.0, null, null));

        SamplerConfig samplerConfig = new SamplerConfig();
        samplerConfig.setMetricsSample(false);
        CapaMetricsSampler.getInstance().update(samplerConfig);
        assertFalse(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1L, null, null));
        assertFalse(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1.0, null, null));


        samplerConfig.setMetricsSample(true);
        CapaMetricsSampler.getInstance().update(samplerConfig);
        assertTrue(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1L, null, null));
        assertTrue(CapaMetricsSampler.getInstance().shouldSampleMeasurement(1.0, null, null));

        CapaMetricsSampler.getInstance().update(SamplerConfig.DEFAULT_CONFIG);

        assertNotNull(CapaTraceSampler.getInstance().getDescription());
    }
}