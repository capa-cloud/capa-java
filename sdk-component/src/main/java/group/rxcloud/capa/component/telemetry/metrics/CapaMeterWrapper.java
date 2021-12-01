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

import group.rxcloud.capa.infrastructure.utils.SpiUtils;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterBuilder;

import javax.annotation.Nullable;

/**
 * Load capa implementation.
 */
final class CapaMeterWrapper {

    static final String FILE_SUFFIX = "telemetry";

    static final boolean CACHE = true;

    private CapaMeterWrapper() {
    }

    @Nullable
    static CapaMeter wrap(String meterName, String schemaUrl, String version, Meter meter) {
        if (meter instanceof CapaMeter) {
            return (CapaMeter) meter;
        }
        CapaMeter result = SpiUtils.loadFromSpiComponentFileNullable(CapaMeter.class,
                new Class[]{String.class, String.class, String.class, Meter.class},
                new Object[]{meterName, schemaUrl, version, meter}, FILE_SUFFIX, CACHE);
        if (result == null) {
            result = new CapaMeter(meterName, schemaUrl, version, meter);
        }
        return result;
    }

    @Nullable
    static CapaMeterBuilder wrap(String meterName, MeterBuilder meterBuilder) {
        if (meterBuilder instanceof CapaMeterBuilder) {
            return (CapaMeterBuilder) meterBuilder;
        }
        return new CapaMeterBuilder(meterName, meterBuilder);
    }

    static <T> T loadHistogramNullable(String meterName, String schemaUrl, String version, String name, String description, String unit, Class<T> type) {
        return SpiUtils.loadFromSpiComponentFileNullable(
                type, new Class[]{String.class, String.class, String.class, String.class, String.class, String.class}, new Object[]{meterName, schemaUrl, version, name, description, unit},
                "telemetry", true);
    }
}

