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
package group.rxcloud.capa.spi.telemetry;

import group.rxcloud.capa.component.telemetry.metrics.CapaMeter;
import io.opentelemetry.api.metrics.Meter;

/**
 * SPI Capa meter.
 */
public abstract class CapaMeterSpi extends CapaMeter {

    /**
     * Instantiates a new Capa meter spi.
     *
     * @param meterName the meter name
     * @param schemaUrl the schema url
     * @param version   the version
     * @param meter     the meter
     */
    public CapaMeterSpi(String meterName, String schemaUrl, String version, Meter meter) {
        super(meterName, schemaUrl, version, meter);
    }
}
