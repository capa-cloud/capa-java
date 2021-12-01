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
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;

import java.util.Collection;
import java.util.function.Supplier;

/**
 *
 */
public abstract class CapaMetricsExporter implements MetricExporter {

    private Supplier<SamplerConfig> samplerConfig;

    public CapaMetricsExporter(Supplier<SamplerConfig> samplerConfig) {
        this.samplerConfig = samplerConfig;
    }

    @Override
    public CompletableResultCode export(Collection<MetricData> metrics) {
        SamplerConfig config = samplerConfig.get();
        if (config != null && !config.isMetricsEnable()) {
            return CompletableResultCode.ofSuccess();
        }

        return doExport(metrics);
    }

    @Override
    public CompletableResultCode flush() {
        SamplerConfig config = samplerConfig.get();
        if (config != null && !config.isMetricsEnable()) {
            return CompletableResultCode.ofSuccess();
        }

        return doFlush();
    }

    protected abstract CompletableResultCode doExport(Collection<MetricData> metrics);

    protected abstract CompletableResultCode doFlush();

    @Override
    public CompletableResultCode shutdown() {
        return null;
    }
}
