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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author: chenyijiang
 * @date: 2021/12/1 19:20
 */
public class CapaMetricsExporterTest {

    @Test
    public void export() {
        TestCapaMetricsExporter exporter = spy(new TestCapaMetricsExporter(() -> SamplerConfig.DEFAULT_CONFIG));
        assertEquals(CompletableResultCode.ofSuccess(), exporter.export(new ArrayList<>()));
        verify(exporter).doExport(anyList());
    }

    @Test
    public void exportWithNullConfig() {
        TestCapaMetricsExporter exporter = spy(new TestCapaMetricsExporter(() -> null));
        assertEquals(CompletableResultCode.ofSuccess(), exporter.export(new ArrayList<>()));
        verify(exporter).doExport(anyList());
    }

    @Test
    public void exportWithDisableConfig() {
        TestCapaMetricsExporter exporter = spy(new TestCapaMetricsExporter(() -> new SamplerConfig() {{ setMetricsEnable(false); setTraceEnable(true);}}));
        assertEquals(CompletableResultCode.ofSuccess(), exporter.export(new ArrayList<>()));
        verify(exporter, never()).doExport(anyList());
    }

    @Test
    public void flushWithNullConfig() {
        TestCapaMetricsExporter exporter = spy(new TestCapaMetricsExporter(() -> null));
        assertEquals(CompletableResultCode.ofSuccess(), exporter.flush());
        verify(exporter).doFlush();
    }

    @Test
    public void flushWithDisableConfig() {
        TestCapaMetricsExporter exporter = spy(new TestCapaMetricsExporter(() -> new SamplerConfig() {{ setMetricsEnable(false); setTraceEnable(true);}}));
        assertEquals(CompletableResultCode.ofSuccess(), exporter.flush());
        verify(exporter, never()).doFlush();
    }

    @Test
    public void flush() {
        TestCapaMetricsExporter exporter = spy(new TestCapaMetricsExporter(() -> SamplerConfig.DEFAULT_CONFIG));
        assertEquals(CompletableResultCode.ofSuccess(), exporter.flush());
        verify(exporter).doFlush();
    }


    @Test
    public void shutdown() {
        assertEquals(CompletableResultCode.ofSuccess(), new TestCapaMetricsExporter(() -> SamplerConfig.DEFAULT_CONFIG).shutdown());
    }
}