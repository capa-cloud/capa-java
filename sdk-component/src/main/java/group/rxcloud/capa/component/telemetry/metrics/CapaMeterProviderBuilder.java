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

/**
 * Builder for capa metric provider.
 */
@NotThreadSafe
public class CapaMeterProviderBuilder implements CapaMeterProviderSettings {

    /**
     * Config for capa metric provider.
     */
    private MeterConfig meterConfigs;

    /**
     * Sampler config.
     */
    private SamplerConfig samplerConfig;

    /**
     * Readers manually set.
     */
    private List<MetricsReaderConfig> metricsReaderConfigs;

    /**
     * Build the reader factories with reader configs.
     * Each factory defined the export interval and exporter of the reader it will generate.
     *
     * @param readerConfigs metrics reader configs.
     * @return metrics reader factories.
     */
    private static List<MetricReaderFactory> bulidReaderFactories(List<MetricsReaderConfig> readerConfigs) {
        List<MetricReaderFactory> factories = new ArrayList<>();
        for (MetricsReaderConfig config : readerConfigs) {
            MetricExporter exporter = SpiUtils
                    .newInstanceWithConstructorCache(config.getExporterType(), MetricExporter.class);
            if (exporter == null) {
                throw new IllegalArgumentException(
                        "Metric Exporter is not configured. readerName = " + config.getName() + '.');
            }

            ScheduledThreadPoolExecutor worker = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(@NotNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("capa-metric-reader-" + config.getName() + '-' + System
                            .currentTimeMillis());
                    return thread;
                }
            });

            factories.add(PeriodicMetricReader.builder(exporter)
                                              .setInterval(config.getExportIntervalMillis(), TimeUnit.MILLISECONDS)
                                              .setExecutor(worker)
                                              .newMetricReaderFactory());
        }
        return factories;
    }

    @Override
    public CapaMeterProviderBuilder setSamplerConfig(SamplerConfig samplerConfig) {
        this.samplerConfig = samplerConfig;
        return this;
    }

    @Override
    public CapaMeterProviderBuilder setMeterConfig(MeterConfig config) {
        meterConfigs = config;
        return this;
    }

    @Override
    public CapaMeterProviderBuilder addMetricReaderConfig(MetricsReaderConfig config) {
        if (metricsReaderConfigs == null) {
            metricsReaderConfigs = new ArrayList<>();
        }
        metricsReaderConfigs.add(config);
        return this;
    }

    /**
     * Build the meter provider with the config.
     * In the following cases, a noop implementation will be returned and no new thread will be started.
     * 1. No metrics reader was defined.
     *
     * @return the meter provider.
     */
    public MeterProvider buildMeterProvider() {
        List<MetricsReaderConfig> metricsReaderConfigs = this.metricsReaderConfigs;
        if (metricsReaderConfigs == null || metricsReaderConfigs.isEmpty()) {
            // if config was not explicitly set, try loading the config from the config loader.
            initMeterConfig();

            if (meterConfigs != null) {
                metricsReaderConfigs = meterConfigs.getReaders();
            }
        }

        if (metricsReaderConfigs == null || metricsReaderConfigs.isEmpty()) {
            return MeterProvider.noop();
        }

        List<MetricReaderFactory> factories = bulidReaderFactories(metricsReaderConfigs);

        initSampleConfig();

        SdkMeterProviderBuilder builder = SdkMeterProvider.builder()
                                                          .setExemplarFilter(CapaMetricsSampler.getInstance()
                                                                                               .update(samplerConfig));
        factories.forEach(f -> builder.registerMetricReader(f));
        return builder.build();
    }

    private void initMeterConfig() {
        if (meterConfigs == null) {
            meterConfigs = SpiUtils.loadConfigNullable(FILE_PATH, MeterConfig.class);
        }
    }

    private void initSampleConfig() {
        if (samplerConfig == null) {
            samplerConfig = SamplerConfig.loadOrDefault();
        }
    }
}
