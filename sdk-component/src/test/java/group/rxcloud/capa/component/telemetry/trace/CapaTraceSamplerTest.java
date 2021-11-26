package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:41
 */
public class CapaTraceSamplerTest {

    @Test
    public void getInstance() {
        assertNotNull(CapaTraceSampler.getInstance());
        CapaTraceSampler.getInstance().update(SamplerConfig.DEFAULT_CONFIG);
        assertEquals(SamplingResult.recordAndSample(), CapaTraceSampler.getInstance().shouldSample(null, null, null, null, null, null));

        CapaTraceSampler.getInstance().update(null);
        assertEquals(SamplingResult.recordAndSample(), CapaTraceSampler.getInstance().shouldSample(null, null, null, null, null, null));

        SamplerConfig samplerConfig = new SamplerConfig();
        samplerConfig.setTraceSample(false);
        CapaTraceSampler.getInstance().update(samplerConfig);
        assertEquals(SamplingResult.drop(), CapaTraceSampler.getInstance().shouldSample(null, null, null, null, null, null));


        samplerConfig.setTraceSample(true);
        CapaTraceSampler.getInstance().update(samplerConfig);
        assertEquals(SamplingResult.recordAndSample(), CapaTraceSampler.getInstance().shouldSample(null, null, null, null, null, null));

        CapaTraceSampler.getInstance().update(SamplerConfig.DEFAULT_CONFIG);

        assertNotNull(CapaTraceSampler.getInstance().getDescription());
    }
}