package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.sdk.trace.IdGenerator;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 22:20
 */
public class TestIdGenerator implements IdGenerator {

    @Override
    public String generateSpanId() {
        return String.valueOf(System.nanoTime());
    }

    @Override
    public String generateTraceId() {
        return String.valueOf(System.nanoTime());
    }
}
