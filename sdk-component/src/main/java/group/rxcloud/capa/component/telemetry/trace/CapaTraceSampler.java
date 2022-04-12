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
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;

/**
 * Sampler for trace data.
 * Choose to sample all or none data according to the config file.
 */
public class CapaTraceSampler implements Sampler {

    private final SamplerConfig samplerConfig;

    public CapaTraceSampler(SamplerConfig samplerConfig) {
        this.samplerConfig = samplerConfig;
    }

    @Override
    public SamplingResult shouldSample(Context context, String traceId, String name, SpanKind kind,
                                       Attributes attributes, List<LinkData> list) {
        Sampler sampler = Sampler.alwaysOn();
        if (samplerConfig != null && !samplerConfig.isTraceEnable()) {
            sampler = Sampler.alwaysOff();
        }
        return sampler.shouldSample(context, traceId, name, kind, attributes, list);
    }

    @Override
    public String getDescription() {
        return "Always or never sample the telemetry data.";
    }
}
