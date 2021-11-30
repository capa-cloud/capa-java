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

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterBuilder;
import io.opentelemetry.api.metrics.MeterProvider;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Builder for capa meter provider.
 */
@NotThreadSafe
public class CapaMeterProvider implements MeterProvider {

    private final MeterProvider meterProvider;

    public CapaMeterProvider(MeterProvider meterProvider) {
        this.meterProvider = meterProvider;
    }

    @Override
    public Meter get(String instrumentationName) {
        return CapaMeterWrapper.wrap(instrumentationName, null, null, meterProvider.get(instrumentationName));
    }

    @Override
    public MeterBuilder meterBuilder(String instrumentationName) {
        return CapaMeterWrapper.wrap(instrumentationName, meterProvider.meterBuilder(instrumentationName));
    }
}
