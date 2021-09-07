package group.rxcloud.capa.infrastructure.exceptions;

import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

/**
 * A Capa's specific exception.
 */
public class CapaExceptions {

    /**
     * Wraps an exception into CapaException (if not already CapaException).
     *
     * @param exception Exception to be wrapped.
     */
    public static void wrap(Throwable exception) {
        if (exception == null) {
            return;
        }

        throw propagate(exception);
    }

    /**
     * Wraps a callable with a try-catch to throw CapaException.
     *
     * @param callable callable to be invoked.
     * @param <T>      type to be returned
     * @return object of type T.
     */
    public static <T> Callable<T> wrap(Callable<T> callable) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception e) {
                wrap(e);
                return null;
            }
        };
    }

    /**
     * Wraps a runnable with a try-catch to throw CapaException.
     *
     * @param runnable runnable to be invoked.
     * @return object of type T.
     */
    public static Runnable wrap(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                wrap(e);
            }
        };
    }

    /**
     * Wraps an exception into CapaException (if not already CapaException).
     *
     * @param exception Exception to be wrapped.
     * @param <T>       Mono's response type.
     * @return Mono containing CapaException.
     */
    public static <T> Mono<T> wrapMono(Exception exception) {
        try {
            wrap(exception);
        } catch (Exception e) {
            return Mono.error(e);
        }
        return Mono.empty();
    }

    /**
     * Wraps an exception into CapaException (if not already CapaException).
     *
     * @param exception Exception to be wrapped.
     * @return wrapped RuntimeException
     */
    public static RuntimeException propagate(Throwable exception) {
        Exceptions.throwIfFatal(exception);

        if (exception instanceof CapaException) {
            return (CapaException) exception;
        }

        if (exception instanceof IllegalArgumentException) {
            return (IllegalArgumentException) exception;
        }

        return new CapaException(exception);
    }
}