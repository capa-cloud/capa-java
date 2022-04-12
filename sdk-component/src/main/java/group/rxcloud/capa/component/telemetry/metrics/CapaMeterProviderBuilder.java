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
import group.rxcloud.capa.infrastructure.CapaClassLoader;
import group.rxcloud.capa.infrastructure.utils.SpiUtils;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.MetricReaderFactory;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Builder for capa metric provider.
 */
@NotThreadSafe
public class CapaMeterProviderBuilder {

    /**
     * Build the meter provider with the config.
     * In the following cases, a noop implementation will be returned and no new thread will be started.
     * 1. No metrics reader was defined.
     *
     * @return the meter provider.
     */
    public MeterProvider buildMeterProvider() {
        MeterConfigLoader meterConfigsLoader = CapaClassLoader.loadComponentClassObj("telemetry-common", MeterConfigLoader.class);

        List<CapaMetricsExporter> metricsReaderConfigs = meterConfigsLoader.getReaders();
        if (metricsReaderConfigs == null || metricsReaderConfigs.isEmpty()) {
            return MeterProvider.noop();
        }

        SamplerConfig samplerConfig = meterConfigsLoader.getSamplerConfig();
        List<MetricReaderFactory> factories = bulidReaderFactories(metricsReaderConfigs, samplerConfig);


        SdkMeterProviderBuilder builder = SdkMeterProvider.builder()
                .setExemplarFilter(new CapaMetricsSampler(samplerConfig));
        factories.forEach(f -> builder.registerMetricReader(f));
        SdkMeterProvider provider = builder.build();
        return new CapaMeterProvider(provider);
    }

    /**
     * Build the reader factories with reader configs.
     * Each factory defined the export interval and exporter of the reader it will generate.
     *
     * @param readerConfigs metrics reader configs.
     * @return metrics reader factories.
     */
    private   List<MetricReaderFactory> bulidReaderFactories(List<CapaMetricsExporter> readerConfigs,
                                                                  SamplerConfig samplerConfig) {
        List<MetricReaderFactory> factories = new ArrayList<>();
        for (CapaMetricsExporter exporter : readerConfigs) {

            ScheduledThreadPoolExecutor worker = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(@NotNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("capa-metric-reader-" + exporter.getName() + '-' + System
                            .currentTimeMillis());
                    return thread;
                }
            });

            factories.add(PeriodicMetricReader.builder(exporter)
                    .setInterval(exporter.getExportIntervalMillis(), TimeUnit.MILLISECONDS)
                    .setExecutor(worker)
                    .newMetricReaderFactory());
        }
        return factories;
    }
}
