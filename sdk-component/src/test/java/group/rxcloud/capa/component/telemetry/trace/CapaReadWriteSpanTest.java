package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
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