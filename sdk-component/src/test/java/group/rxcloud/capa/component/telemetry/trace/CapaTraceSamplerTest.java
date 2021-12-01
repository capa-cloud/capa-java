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
package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:41
 */
public class CapaTraceSamplerTest {

    @Test
    public void testConfig() {
        CapaTraceSampler sampler = new CapaTraceSampler(() -> SamplerConfig.DEFAULT_CONFIG);
        assertEquals(SamplingResult.recordAndSample(), sampler.shouldSample(null, null, null, null, null, null));
        sampler = new CapaTraceSampler(() -> null);
        assertEquals(SamplingResult.recordAndSample(), sampler.shouldSample(null, null, null, null, null, null));
        SamplerConfig config = new SamplerConfig();
        config.setTraceEnable(false);
        sampler = new CapaTraceSampler(() -> config);
        assertEquals(SamplingResult.drop(), sampler.shouldSample(null, null, null, null, null, null));
        config.setTraceEnable(true);
        assertEquals(SamplingResult.recordAndSample(), sampler.shouldSample(null, null, null, null, null, null));
    }

    @Test
    public void getDescription() {
        assertNotNull(new CapaTraceSampler(() -> SamplerConfig.DEFAULT_CONFIG).getDescription());
    }
}