package group.rxcloud.capa.examples.telemetry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.text.SimpleDateFormat;
import java.util.Collection;

public class ConsoleSpanExporter implements SpanExporter {

    /**
     * Shared Json serializer/deserializer as per Jackson's documentation.
     */
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final String name;

    private final boolean onlyOutputId;

    public ConsoleSpanExporter() {
        this(ConsoleSpanExporter.class.getSimpleName(), false);
    }

    public ConsoleSpanExporter(String name) {
        this(name, false);
    }

    public ConsoleSpanExporter(String name, boolean onlyOutputId) {
        this.name = name;
        this.onlyOutputId = onlyOutputId;
    }

    @Override
    public CompletableResultCode export(Collection<SpanData> collection) {
        // X-Ray
        System.out.println("--------------------------------- Trace Export ---------------------------------");
        System.out.println(Thread.currentThread().getName() + " " + name + " batchSize=" + collection.size());
        collection.forEach(s -> {
            try {
                if (onlyOutputId) {
                    System.out.println(name + ": {");
                    System.out.println("spanName: " + s.getName());
                    System.out.println("traceId: " + s.getTraceId());
                    System.out.println("spanId: " + s.getSpanId());
                    System.out.println("parentSpanId: " + s.getParentSpanId());
                    System.out.println("}");
                } else {
                    System.out.println(
                            name + ":  " + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(s));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        System.out.println("--------------------------------- Trace Export ---------------------------------");
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        System.out.println(name + " flush");
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode shutdown() {
        flush();
        System.out.println(name + " shoutdown");
        return CompletableResultCode.ofSuccess();
    }
}