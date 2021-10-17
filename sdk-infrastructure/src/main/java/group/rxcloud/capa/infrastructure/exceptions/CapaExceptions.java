/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.infrastructure.exceptions;

import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
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
     * @param <T>       Flux's response type.
     * @return Flux containing CapaException.
     */
    public static <T> Flux<T> wrapFlux(Exception exception) {
        try {
            wrap(exception);
        } catch (Exception e) {
            return Flux.error(e);
        }
        return Flux.empty();
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
