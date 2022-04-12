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

import com.google.common.collect.Lists;
import group.rxcloud.capa.component.telemetry.SamplerConfig;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.metrics.internal.NoopMeterProvider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 17:31
 */
public class CapaMeterProviderBuilderTest {


    @Test
    public void buildWithConfigFile() {

        MeterProvider meterProvider = new CapaMeterProviderBuilder()
                .buildMeterProvider();

        ThreadGroup currentGroup =
                Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);

        assertTrue(Arrays.stream(lstThreads).anyMatch(t -> t.getName().contains("my-reader")));
    }


    @Test
    public void buildWithEmptyConfig() {

        MeterProvider meterProvider = new CapaMeterProviderBuilder()
                .setMeterConfig(new MeterConfigLoader())
                .buildMeterProvider();

        assertTrue(meterProvider instanceof NoopMeterProvider);
    }

    @Test
    public void buildWithUnknownExporterConfig() {
        Throwable throwable = null;
        try {
            new CapaMeterProviderBuilder()
                    .addMetricReaderConfig(new MetricsReaderConfig() {{
                        setExporterType("aaaaa");
                    }})
                    .buildMeterProvider();
        } catch (Throwable throwable1) {
            throwable = throwable1;
        }

        assertTrue(throwable instanceof IllegalArgumentException);
    }

    @Test
    public void addReaderConfig() {
        MetricsReaderConfig readerConfigByInstance = new MetricsReaderConfig();
        readerConfigByInstance.setName("reader_a2");
        readerConfigByInstance.setExportInterval(2, TimeUnit.SECONDS);
        readerConfigByInstance.setExporterType(TestMetricsExporter.class.getName());
        MetricsReaderConfig readerConfigByPath = new MetricsReaderConfig();
        readerConfigByPath.setName("reader_b2");
        readerConfigByPath.setExportInterval(4, TimeUnit.SECONDS);
        readerConfigByPath.setExporterType("group.rxcloud.capa.component.telemetry.metrics.TestMetricsExporter");

        MeterProvider meterProvider = new CapaMeterProviderBuilder()
                .addMetricReaderConfig(readerConfigByInstance)
                .addMetricReaderConfig(readerConfigByPath)
                .buildMeterProvider();

        ThreadGroup currentGroup =
                Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);

        assertTrue(Arrays.stream(lstThreads).anyMatch(t -> t.getName().contains(readerConfigByInstance.getName())));
        assertTrue(Arrays.stream(lstThreads).anyMatch(t -> t.getName().contains(readerConfigByPath.getName())));
    }


    @Test
    public void setMeterConfig() {
        MeterConfigLoader meterConfigLoader = new MeterConfigLoader();
        MetricsReaderConfig readerConfigByInstance = new MetricsReaderConfig();
        readerConfigByInstance.setName("reader_a3");
        readerConfigByInstance.setExportInterval(2, TimeUnit.SECONDS);
        readerConfigByInstance.setExporterType(TestMetricsExporter.class.getName());
        meterConfigLoader.setReaders(Lists.newArrayList(readerConfigByInstance));

        MetricsReaderConfig readerConfigByPath = new MetricsReaderConfig();
        readerConfigByPath.setName("reader_b3");
        readerConfigByPath.setExportInterval(4, TimeUnit.SECONDS);
        readerConfigByPath.setExporterType("group.rxcloud.capa.component.telemetry.metrics.TestMetricsExporter");

        SamplerConfig samplerConfig = new SamplerConfig();
        samplerConfig.setMetricsEnable(false);

        MeterProvider meterProvider = new CapaMeterProviderBuilder()
                .addMetricReaderConfig(readerConfigByPath)
                .setMeterConfig(meterConfigLoader)
                .setSamplerConfig(() -> samplerConfig)
                .buildMeterProvider();

        ThreadGroup currentGroup =
                Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);

        assertTrue(Arrays.stream(lstThreads).noneMatch(t -> t.getName().contains(readerConfigByInstance.getName())));
        assertTrue(Arrays.stream(lstThreads).anyMatch(t -> t.getName().contains(readerConfigByPath.getName())));

    }
}