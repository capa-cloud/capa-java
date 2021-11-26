package group.rxcloud.capa.component.telemetry.context;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 20:55
 */
public class TestCapaContextAsyncWrapper implements CapaContextAsyncWrapper {

    private static Set<Class> called = new HashSet<>();

    @Override
    public Runnable wrap(Runnable runnable) {
        called.add(Runnable.class);
        return CapaContextAsyncWrapper.super.wrap(runnable);
    }

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        called.add(Callable.class);
        return CapaContextAsyncWrapper.super.wrap(callable);
    }

    @Override
    public Executor wrap(Executor executor) {
        called.add(Executor.class);
        return CapaContextAsyncWrapper.super.wrap(executor);
    }

    @Override
    public ExecutorService wrap(ExecutorService executor) {
        called.add(ExecutorService.class);
        return CapaContextAsyncWrapper.super.wrap(executor);
    }

    @Override
    public ScheduledExecutorService wrap(ScheduledExecutorService executor) {
        called.add(ScheduledExecutorService.class);
        return CapaContextAsyncWrapper.super.wrap(executor);
    }

    @Override
    public String getTraceId() {
        return CapaContextAsyncWrapper.super.getTraceId();
    }

    public static boolean called(Class type) {
        return called.contains(type);
    }
}
