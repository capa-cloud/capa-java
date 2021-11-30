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
package group.rxcloud.capa.component.telemetry;

import group.rxcloud.capa.infrastructure.utils.SpiUtils;

import java.io.Serializable;
import java.util.Properties;

/**
 * Sampler config.
 */
public class SamplerConfig implements Serializable {

    public static final String FILE_PATH = "/capa-sample.properties";

    /**
     * Sample all data as default.
     */
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig();

    private static final long serialVersionUID = -2113523925814197551L;

    private boolean metricsSample = true;

    private boolean traceSample = true;

    private boolean logSample = true;

    public boolean isMetricsSample() {
        return metricsSample;
    }

    public void setMetricsSample(boolean metricsSample) {
        this.metricsSample = metricsSample;
    }

    public boolean isTraceSample() {
        return traceSample;
    }

    public void setTraceSample(boolean traceSample) {
        this.traceSample = traceSample;
    }

    public boolean isLogSample() {
        return logSample;
    }

    public void setLogSample(boolean logSample) {
        this.logSample = logSample;
    }

    public static SamplerConfig loadOrDefault() {
        Properties properties = SpiUtils.loadPropertiesNullable(FILE_PATH);
        if (properties == null) {
            return DEFAULT_CONFIG;
        }

        SamplerConfig result = new SamplerConfig();
        result.setMetricsSample(Boolean.valueOf(properties.getProperty("metricsSample", Boolean.TRUE.toString())));
        result.setTraceSample(Boolean.valueOf(properties.getProperty("traceSample", Boolean.TRUE.toString())));
        result.setLogSample(Boolean.valueOf(properties.getProperty("logSample", Boolean.TRUE.toString())));

        return result;
    }
}
