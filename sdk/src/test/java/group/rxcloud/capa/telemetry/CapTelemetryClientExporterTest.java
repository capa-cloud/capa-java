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
package group.rxcloud.capa.telemetry;

import io.opentelemetry.api.GlobalOpenTelemetry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/26 14:12
 */
public class CapTelemetryClientExporterTest {

    @Test
    public void getOrCreate() {
        CapaTelemetryClient client = new CapaTelemetryClientBuilder().build();
        assertTrue(client instanceof CapTelemetryClientExporter);
        assertNotNull(client.getContextPropagators());
        assertNotNull(((CapTelemetryClientExporter) client).getPropagators());
        assertNotNull(((CapTelemetryClientExporter) client).getMeterProvider());
        assertNotNull(((CapTelemetryClientExporter) client).getTracerProvider());
        assertNotNull(GlobalOpenTelemetry.get());
    }
}