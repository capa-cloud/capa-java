package group.rxcloud.capa.telemetry;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

public class TraceProcessor implements SpanProcessor {

    @Override
    public void onStart(Context context, ReadWriteSpan span) {

    }

    @Override
    public boolean isStartRequired() {
        return false;
    }

    @Override
    public void onEnd(ReadableSpan span) {
        System.out.println(span.toSpanData());
    }

    @Override
    public boolean isEndRequired() {
        return true;
    }
}