package group.rxcloud.capa.examples.telemetry;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

/**
 * @author: chenyijiang
 * @date: 2021/11/14 17:05
 */
public class ConsoleSpanProcessor implements SpanProcessor {

    private SpanProcessor processor = SimpleSpanProcessor.create(new ConsoleSpanExporter());

    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {
        processor.onStart(parentContext, span);
    }

    @Override
    public boolean isStartRequired() {
        return processor.isStartRequired();
    }

    @Override
    public void onEnd(ReadableSpan span) {
        processor.onEnd(span);
    }

    @Override
    public boolean isEndRequired() {
        return processor.isEndRequired();
    }
}
