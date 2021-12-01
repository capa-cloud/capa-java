/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.component.telemetry.metrics;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 17:16
 */
public class CapaMetricsSamplerTest {

    @Test
    public void testConfig() {
        CapaMetricsSampler sampler = new CapaMetricsSampler(() -> SamplerConfig.DEFAULT_CONFIG);
        assertTrue(sampler.shouldSampleMeasurement(1, null, null));
        sampler = new CapaMetricsSampler(() -> null);
        assertTrue(sampler.shouldSampleMeasurement(1.0, null, null));
        SamplerConfig config = new SamplerConfig();
        config.setMetricsEnable(false);
        sampler = new CapaMetricsSampler(() -> config);
        assertFalse(sampler.shouldSampleMeasurement(1, null, null));
        config.setMetricsEnable(true);
        assertTrue(sampler.shouldSampleMeasurement(1.0, null, null));
    }

}