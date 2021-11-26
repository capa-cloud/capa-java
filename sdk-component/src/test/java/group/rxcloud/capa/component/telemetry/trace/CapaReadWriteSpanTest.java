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
package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.sdk.trace.ReadWriteSpan;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 22:48
 */
public class CapaReadWriteSpanTest {

    @Test
    public void getTracerName() {
        ReadWriteSpan span = Mockito.mock(ReadWriteSpan.class);
        CapaReadWriteSpan readWriteSpan = new CapaReadWriteSpan("aaa", "1.1", "url", span);

        readWriteSpan.getName();
        verify(span).getName();

        readWriteSpan.getKind();
        verify(span).getKind();

        readWriteSpan.getParentSpanContext();
        verify(span).getParentSpanContext();

        assertEquals("aaa", readWriteSpan.getTracerName());
        verify(span, never()).getInstrumentationLibraryInfo();

        assertEquals("1.1", readWriteSpan.getVersion());
        verify(span, never()).getInstrumentationLibraryInfo();

        assertEquals("url", readWriteSpan.getSchemaUrl());
        verify(span, never()).getInstrumentationLibraryInfo();

        long mills = System.currentTimeMillis();
        readWriteSpan.addEvent("aaa", null, mills, TimeUnit.MILLISECONDS);
        verify(span).addEvent("aaa", null, mills, TimeUnit.MILLISECONDS);

        readWriteSpan.updateName("lll");
        verify(span).updateName("lll");

        readWriteSpan.toSpanData();
        verify(span).toSpanData();

        readWriteSpan.getInstrumentationLibraryInfo();
        verify(span).getInstrumentationLibraryInfo();

        doReturn(true).when(span).hasEnded();
        assertTrue(readWriteSpan.hasEnded());

        doReturn(System.nanoTime()).when(span).getLatencyNanos();
        readWriteSpan.getLatencyNanos();
        verify(span).getLatencyNanos();

        readWriteSpan.getAttribute(StatusAttributeKey.STATUS);
        verify(span).getAttribute(StatusAttributeKey.STATUS);


        doReturn(true).when(span).isRecording();
        assertTrue(readWriteSpan.isRecording());

    }
}