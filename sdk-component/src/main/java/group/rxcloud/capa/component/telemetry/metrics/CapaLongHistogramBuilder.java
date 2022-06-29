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

import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.LongHistogramBuilder;
import io.opentelemetry.api.metrics.internal.NoopMeter;

public class CapaLongHistogramBuilder implements LongHistogramBuilder {

    private final String meterName;

    private final String schemaUrl;

    private final String version;

    private final String name;

    private String description;

    private String unit;

    public CapaLongHistogramBuilder(String meterName, String schemaUrl, String version, String name) {
        this.meterName = meterName;
        this.schemaUrl = schemaUrl;
        this.version = version;
        this.name = name;
    }

    @Override
    public LongHistogramBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public LongHistogramBuilder setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public DoubleHistogramBuilder ofDoubles() {
        return new CapaDoubleHistogramBuilder(meterName, schemaUrl, version, name).setDescription(description)
                .setUnit(unit);
    }

    @Override
    public LongHistogram build() {
        LongHistogram histogram = CapaMeterWrapper
                .loadHistogramNullable(meterName, schemaUrl, version, name, description, unit, CapaLongHistogram.class);
        if (histogram == null) {
            return NoopMeter.getInstance().histogramBuilder(name).ofLongs().build();
        }
        return histogram;
    }
}
