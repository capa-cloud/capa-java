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

import group.rxcloud.capa.component.telemetry.SpiUtils;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.exemplar.ExemplarFilter;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.MetricReaderFactory;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class CapaMeterProviderBuilder implements CapaMeterProviderSettings {

    private MeterConfig meterConfigs;

    public MeterConfig getMetricsConfigs() {
        return meterConfigs;
    }

    @Override
    public CapaMeterProviderSettings setMetricsConfig(MeterConfig config) {
        meterConfigs = meterConfigs;
        return this;
    }

    @Override
    public CapaMeterProviderSettings addMetricReaderConfig(MetricsReaderConfig config) {
        if (meterConfigs == null) {
            meterConfigs = new MeterConfig();
            meterConfigs.setReaders(new ArrayList<>());
        }
        meterConfigs.getReaders().add(config);
        return this;
    }


    private void initMeterConfig() {
        if (meterConfigs == null) {
            CapaMeterConfigLoader meterConfigLoader = SpiUtils.getFromSpiConfigFile(CapaMeterConfigLoader.class);
            if (meterConfigLoader == null) {
                meterConfigLoader = CapaMeterConfigLoader.DEFAULT;
            }
            meterConfigs = meterConfigLoader.loadMetricsConfig();
        }
    }

    public MeterProvider buildMeterProvider() {
        // if config was not explicitly set, try loading the config from the config loader.
        initMeterConfig();

        MeterConfig meterConfigs = this.meterConfigs;
        if (meterConfigs == null) {
            return MeterProvider.noop();
        }
        List<MetricsReaderConfig> readerConfigs = this.meterConfigs.getReaders();
        if (readerConfigs == null || readerConfigs.isEmpty()) {
            return MeterProvider.noop();
        }

        List<MetricReaderFactory> factories = bulidReaderFactories(readerConfigs);
        if (factories.isEmpty()) {
            return MeterProvider.noop();
        }

        SdkMeterProviderBuilder builder = SdkMeterProvider.builder()
                                                          .setExemplarFilter(ExemplarFilter.alwaysSample());
        factories.forEach(f -> builder.registerMetricReader(f));
        return builder.build();
    }

    private List<MetricReaderFactory> bulidReaderFactories(List<MetricsReaderConfig> readerConfigs) {
        if (readerConfigs == null || readerConfigs.isEmpty()) {
            return Collections.emptyList();
        }
        List<MetricReaderFactory> factories = new ArrayList<>();
        for (MetricsReaderConfig config : readerConfigs) {
            if (config.isDisable()) {
                continue;
            }

            MetricExporter exporter = config.getExporterInstance() != null ? config.getExporterInstance()
                    : SpiUtils.getFromSpiConfigFile(config.getExporterType(), MetricExporter.class);
            if (exporter == null) {
                throw new IllegalArgumentException(
                        "Metric Exporter is not configured. readerName = " + config.getReaderName() + '.');
            }

            ScheduledThreadPoolExecutor worker = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(@NotNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("capa-metric-reader-" + config.getReaderName() + '-' + System
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
}
