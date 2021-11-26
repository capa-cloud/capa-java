package group.rxcloud.capa.component.telemetry.context;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 20:53
 */
public class CapaContextTest {

    @Test
    public void taskWrapping() {
        CapaContext.taskWrapping(new Runnable() {
            @Override
            public void run() {

            }
        });
        assertTrue(TestCapaContextAsyncWrapper.called(Runnable.class));
        CapaContext.taskWrapping(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });
        assertTrue(TestCapaContextAsyncWrapper.called(Callable.class));
        CapaContext.taskWrapping(Executors.newSingleThreadExecutor());
        assertTrue(TestCapaContextAsyncWrapper.called(ExecutorService.class));
        CapaContext.taskWrapping(new Executor() {
            @Override
            public void execute(@NotNull Runnable command) {

            }
        });
        assertTrue(TestCapaContextAsyncWrapper.called(Executor.class));
        CapaContext.taskWrapping(Executors.newScheduledThreadPool(1));
        assertTrue(TestCapaContextAsyncWrapper.called(ScheduledExecutorService.class));

        assertNotNull(CapaContext.getTraceId());
    }
}