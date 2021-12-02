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
import io.opentelemetry.api.metrics.internal.NoopMeter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author: chenyijiang
 * @date: 2021/12/1 19:55
 */
public class CapaMeterTest {

    @Test
    public void counterBuilder() {
        Meter meter = mock(Meter.class);
        CapaMeter capaMeter = new CapaMeter("", "", "", meter);
        capaMeter.counterBuilder("aaa");
        verify(meter).counterBuilder("aaa");
    }

    @Test
    public void upDownCounterBuilder() {
        Meter meter = mock(Meter.class);
        CapaMeter capaMeter = new CapaMeter("", "", "", meter);
        capaMeter.upDownCounterBuilder("aaa");
        verify(meter).upDownCounterBuilder("aaa");
    }

    @Test
    public void histogramBuilder() {
        Meter meter = mock(Meter.class);
        CapaMeter capaMeter = new CapaMeter("", "", "", meter);
        assertTrue(capaMeter.histogramBuilder("aaa").setUnit("a").setDescription("desc").build() instanceof NoopMeter.NoopDoubleHistogram);
        verify(meter, never()).histogramBuilder("aaa");
    }

    @Test
    public void gaugeBuilder() {
        Meter meter = mock(Meter.class);
        CapaMeter capaMeter = new CapaMeter("", "", "", meter);
        capaMeter.gaugeBuilder("aaa");
        verify(meter).gaugeBuilder("aaa");
    }
}