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

import io.opentelemetry.sdk.metrics.export.MetricExporter;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Config for metrics reader.
 * Control the export interval and target exporter.
 */
public class MetricsReaderConfig implements Serializable {

    private static final long serialVersionUID = 5186270483151262376L;

    /**
     * Reader name, used to find the related reader thread.
     */
    private String name = "_DEFAULT_METRIC_READER";

    /**
     * Exporter interval.
     * default 1min.
     */
    private long exportIntervalMillis = TimeUnit.MINUTES.toMillis(1L);

    /**
     * Exporter class name. Must have a no-args constructor.
     */
    private String exporterType;

    public String getExporterType() {
        return exporterType;
    }

    public void setExporterType(String exporterType) {
        this.exporterType = exporterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExportIntervalMillis() {
        return exportIntervalMillis;
    }

    public void setExportIntervalMillis(long exportIntervalMillis) {
        this.exportIntervalMillis = exportIntervalMillis;
    }

    public void setExportInterval(long export, TimeUnit timeUnit) {
        setExportIntervalMillis(timeUnit.toMillis(export));
    }
}
