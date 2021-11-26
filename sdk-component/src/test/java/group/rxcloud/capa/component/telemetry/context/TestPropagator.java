package group.rxcloud.capa.component.telemetry.context;

import com.google.common.collect.Lists;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:07
 */
public class TestPropagator implements TextMapPropagator {

    private static AtomicInteger called = new AtomicInteger();

    @Override
    public Collection<String> fields() {
        return Lists.newArrayList("a");
    }

    @Override
    public <C> void inject(Context context, @Nullable C carrier, TextMapSetter<C> setter) {
        called.incrementAndGet();
    }

    @Override
    public <C> Context extract(Context context, @Nullable C carrier, TextMapGetter<C> getter) {
        return null;
    }

    public static int getCalled() {
        return called.get();
    }

    public static void clean() {
        called.set(0);
    }
}
