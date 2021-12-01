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

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author: chenyijiang
 * @date: 2021/12/1 19:20
 */
public class TestCapaMetricsExporter extends CapaMetricsExporter {

    public TestCapaMetricsExporter(
            Supplier<SamplerConfig> samplerConfig) {
        super(samplerConfig);
    }

    @Override
    protected CompletableResultCode doExport(Collection<MetricData> metrics) {
        return CompletableResultCode.ofSuccess();
    }

    @Override
    protected CompletableResultCode doFlush() {
        return  CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
    }
}