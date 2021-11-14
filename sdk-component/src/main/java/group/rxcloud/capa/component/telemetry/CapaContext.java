package group.rxcloud.capa.component.telemetry;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:10
 */
public final class CapaContext {

    private static final CapaContextAsyncWrapper WRAPPER = getAsyncWrapper();

    public static Runnable taskWrapping(Runnable runnable) {
        return WRAPPER.wrap(runnable);
    }

    public static <T> Callable<T> taskWrapping(Callable<T> callable) {
        return WRAPPER.wrap(callable);
    }

    public static Executor taskWrapping(Executor executor) {
        return WRAPPER.wrap(executor);
    }

    public static ExecutorService taskWrapping(ExecutorService executor) {
        return WRAPPER.wrap(executor);
    }

    public static ScheduledExecutorService taskWrapping(ScheduledExecutorService executor) {
        return WRAPPER.wrap(executor);
    }

    private static CapaContextAsyncWrapper getAsyncWrapper() {
        CapaContextAsyncWrapper WRAPPER = SpiUtils.getFromSpiConfigFile(CapaContextAsyncWrapper.class);
        if (WRAPPER == null) {
            return new CapaContextAsyncWrapper() {};
        }
        return WRAPPER;
    }
}
