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

import io.opentelemetry.api.metrics.DoubleGaugeBuilder;
import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongCounterBuilder;
import io.opentelemetry.api.metrics.LongUpDownCounterBuilder;
import io.opentelemetry.api.metrics.Meter;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class CapaMeter implements Meter {

    protected final String meterName;

    protected final String schemaUrl;

    protected final String version;

    protected final Meter meter;

    public CapaMeter(String meterName, String schemaUrl, String version, Meter meter) {
        this.meterName = meterName;
        this.schemaUrl = schemaUrl;
        this.version = version;
        this.meter = meter;
    }

    @Override
    public LongCounterBuilder counterBuilder(String name) {
        return meter.counterBuilder(name);
    }

    @Override
    public LongUpDownCounterBuilder upDownCounterBuilder(String name) {
        return meter.upDownCounterBuilder(name);
    }

    /**
     * Only histogram need self inplementation now.
     *
     * @param name metric name.
     * @return CapaDoubleHistogramBuilder
     */
    @Override
    public DoubleHistogramBuilder histogramBuilder(String name) {
        return new CapaDoubleHistogramBuilder(meterName, schemaUrl, version, name);
    }

    @Override
    public DoubleGaugeBuilder gaugeBuilder(String name) {
        return meter.gaugeBuilder(name);
    }
}
