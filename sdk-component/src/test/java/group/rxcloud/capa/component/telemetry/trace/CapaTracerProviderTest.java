package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 22:35
 */
public class CapaTracerProviderTest {

    @Test
    public void get() {
        Tracer tracer = new CapaTracerProviderBuilder().buildTracerProvider().get("aaa");
        assertTrue(tracer instanceof CapaTracer);
        assertEquals("aaa", ((CapaTracer) tracer).tracerName);

        tracer = new CapaTracerProviderBuilder().buildTracerProvider().get("aaa", "1.1");
        assertTrue(tracer instanceof CapaTracer);
        assertEquals("aaa", ((CapaTracer) tracer).tracerName);
        assertEquals("1.1", ((CapaTracer) tracer).getVersion());


        tracer = new CapaTracerProviderBuilder().buildTracerProvider().tracerBuilder("aaa").setSchemaUrl("url").setInstrumentationVersion("1.1").build();
        assertTrue(tracer instanceof CapaTracer);
        assertEquals("aaa", ((CapaTracer) tracer).tracerName);
        assertEquals("1.1", ((CapaTracer) tracer).getVersion());
        assertEquals("url", ((CapaTracer) tracer).getSchemaUrl());
    }
}