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

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Sampler config.
 */
public class SamplerConfig implements Serializable {

    /**
     * Sample all data as default.
     */
    public static final transient SamplerConfig DEFAULT_CONFIG = new SamplerConfig() {{
        setTraceEnable(true);
        setMetricsEnable(true);
    }};

    public static final transient Supplier<SamplerConfig> DEFAULT_SUPPLIER = () -> DEFAULT_CONFIG;

    private Boolean metricsEnable;

    private Boolean traceEnable;

    public Boolean isMetricsEnable() {
        return metricsEnable == null ? DEFAULT_SUPPLIER.get().metricsEnable : metricsEnable;
    }

    public void setMetricsEnable(boolean metricsEnable) {
        this.metricsEnable = metricsEnable;
    }

    public Boolean isTraceEnable() {
        return traceEnable == null ? DEFAULT_SUPPLIER.get().traceEnable : traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
}
