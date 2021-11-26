package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:52
 */
public class TestSpanProcessor implements SpanProcessor {

    static Set<String> spanNames = new HashSet<>();

    @Override
    public void onStart(Context context, ReadWriteSpan span) {

    }

    @Override
    public boolean isStartRequired() {
        return false;
    }

    @Override
    public void onEnd(ReadableSpan span) {
        spanNames.add(span.getName());
    }

    @Override
    public boolean isEndRequired() {
        return false;
    }

    public static boolean called(String name) {
        return spanNames.contains(name);
    }
}
