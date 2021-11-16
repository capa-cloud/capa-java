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
 */
public class MetricsReaderConfig implements Serializable {

    private static final long serialVersionUID = 5186270483151262376L;

    private String readerName = "UNKNOWN";

    private boolean disable;

    // default 1min.
    private long exportIntervalMillis = TimeUnit.MINUTES.toMillis(1L);
    
    private String exporterType;
    
    private transient MetricExporter exporterInstance;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public MetricExporter getExporterInstance() {
        return exporterInstance;
    }

    public void setExporterInstance(MetricExporter exporterInstance) {
        this.exporterInstance = exporterInstance;
    }

    public String getExporterType() {
        return exporterType;
    }

    public void setExporterType(String exporterType) {
        this.exporterType = exporterType;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public long getExportIntervalMillis() {
        return exportIntervalMillis;
    }

    public void setExportIntervalMillis(long exportIntervalMillis) {
        this.exportIntervalMillis = exportIntervalMillis;
    }

    public void setExportMillis(long export, TimeUnit timeUnit) {
        this.exportIntervalMillis = timeUnit.toMillis(export);
    }
}
