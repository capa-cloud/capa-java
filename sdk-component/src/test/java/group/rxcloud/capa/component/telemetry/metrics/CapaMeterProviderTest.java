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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 22:35
 */
public class CapaMeterProviderTest {

    @Test
    public void meterBuilder() {
        Meter meter = new CapaMeterProviderBuilder().buildMeterProvider().meterBuilder("aaa").setSchemaUrl("url")
                                                    .setInstrumentationVersion("1.1").build();
        assertTrue(meter instanceof CapaMeter);
        assertEquals("aaa", ((CapaMeter) meter).meterName);
        assertEquals("1.1", ((CapaMeter) meter).version);
        assertEquals("url", ((CapaMeter) meter).schemaUrl);
    }


    @Test
    public void get() {
        Meter meter = new CapaMeterProviderBuilder().buildMeterProvider().get("aaa");
        assertTrue(meter instanceof CapaMeter);
        assertEquals("aaa", ((CapaMeter) meter).meterName);
        assertNull(((CapaMeter) meter).version);
        assertNull(((CapaMeter) meter).schemaUrl);
    }
}