package group.rxcloud.capa.component.telemetry;

import io.opentelemetry.context.Context;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 21:10
 */
public interface CapaContextAsyncWrapper {

    default Runnable wrap(Runnable runnable) {
        return Context.current().wrap(runnable);
    }

    default <T> Callable<T> wrap(Callable<T> callable) {
        return Context.current().wrap(callable);
    }

    default Executor wrap(Executor executor) {
        return Context.current().wrap(executor);
    }

    default ExecutorService wrap(ExecutorService executor) {
        return Context.current().wrap(executor);
    }

    default ScheduledExecutorService wrap(ScheduledExecutorService executor) {
        return Context.current().wrap(executor);
    }

}
