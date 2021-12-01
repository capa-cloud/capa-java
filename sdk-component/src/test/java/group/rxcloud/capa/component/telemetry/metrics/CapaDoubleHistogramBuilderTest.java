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
import io.opentelemetry.api.metrics.LongHistogramBuilder;
import io.opentelemetry.api.metrics.internal.NoopMeter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: chenyijiang
 * @date: 2021/12/1 20:05
 */
public class CapaDoubleHistogramBuilderTest {

    @Test
    public void build() {
        CapaDoubleHistogramBuilder builder = new CapaDoubleHistogramBuilder("", "", "", "");
        assertEquals(builder, builder.setDescription("aaa"));
        assertEquals(builder, builder.setUnit("bbb"));
        assertTrue(builder.build() instanceof NoopMeter.NoopDoubleHistogram);

        LongHistogramBuilder longHistogramBuilder = builder.ofLongs();
        assertEquals(longHistogramBuilder, longHistogramBuilder.setDescription("ccc"));
        assertEquals(longHistogramBuilder, longHistogramBuilder.setUnit("ddd"));
        assertTrue(longHistogramBuilder.build() instanceof TestLongHistogram);

        DoubleHistogramBuilder doubleHistogramBuilder = longHistogramBuilder.ofDoubles();
        assertTrue(doubleHistogramBuilder instanceof CapaDoubleHistogramBuilder);
    }
}