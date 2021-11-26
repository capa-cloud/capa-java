package group.rxcloud.capa.telemetry;

import io.opentelemetry.api.GlobalOpenTelemetry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: chenyijiang
 * @date: 2021/11/26 14:12
 */
public class CapaTelemetryClientGlobalTest {

    @Test
    public void getOrCreate() {
        CapaTelemetryClient client = CapaTelemetryClientGlobal.getOrCreate();
        assertTrue(client instanceof CapaTelemetryClientGlobal);
        assertNotNull(client.getContextPropagators());
        assertNotNull(((CapaTelemetryClientGlobal) client).getPropagators());
        assertNotNull(((CapaTelemetryClientGlobal) client).getMeterProvider());
        assertNotNull(((CapaTelemetryClientGlobal) client).getTracerProvider());
        assertNotNull(GlobalOpenTelemetry.get());
        assertEquals(client, CapaTelemetryClientGlobal.getOrCreate());
    }
}