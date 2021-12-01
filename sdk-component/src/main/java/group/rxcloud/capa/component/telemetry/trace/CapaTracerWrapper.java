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
package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.infrastructure.utils.SpiUtils;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerBuilder;
import io.opentelemetry.sdk.trace.ReadWriteSpan;

import javax.annotation.Nullable;

/**
 * Load capa implementation.
 */
final class CapaTracerWrapper {
    
    static final String FILE_SUFFIX = "telemetry";

    static final boolean CACHE = true;

    private CapaTracerWrapper() {
    }

    @Nullable
    static CapaSpanBuilder wrap(String tracerName, String version, String schemaUrl, String spanName, SpanBuilder builder) {
        if (builder instanceof CapaSpanBuilder) {
            return (CapaSpanBuilder) builder;
        }
        CapaSpanBuilder result = SpiUtils
                .loadFromSpiComponentFileNullable(CapaSpanBuilder.class, new Class[]{String.class, String.class, String.class, String.class, SpanBuilder.class},
                        new Object[]{tracerName, version, schemaUrl, spanName, builder}, FILE_SUFFIX, CACHE);
        if (result == null) {
            result = new CapaSpanBuilder(tracerName, version, schemaUrl, spanName, builder);
        }
        return result;
    }

    @Nullable
    static CapaReadWriteSpan wrap(String tracerName, String version, String schemaUrl, ReadWriteSpan span) {
        if (span instanceof CapaReadWriteSpan) {
            return (CapaReadWriteSpan) span;
        }
        CapaReadWriteSpan result = SpiUtils
                .loadFromSpiComponentFileNullable(CapaReadWriteSpan.class, new Class[]{String.class, String.class, String.class, ReadWriteSpan.class}, new Object[]{tracerName, version, schemaUrl, span},
                        FILE_SUFFIX, CACHE);
        if (result == null) {
            result = new CapaReadWriteSpan(tracerName, version, schemaUrl, span);
        }
        return result;
    }

    @Nullable
    static CapaTracer wrap(String tracerName, String version, String schemaUrl, Tracer tracer) {
        if (tracer instanceof CapaTracer) {
            return (CapaTracer) tracer;
        }
        CapaTracer result = SpiUtils.loadFromSpiComponentFileNullable(CapaTracer.class, new Class[]{String.class, String.class, String.class, Tracer.class},
                new Object[]{tracerName, version, schemaUrl, tracer}, FILE_SUFFIX, CACHE);
        if (result == null) {
            result = new CapaTracer(tracerName, version, schemaUrl, tracer);
        }
        return result;
    }

    @Nullable
    static CapaTracerBuilder wrap(String tracerName, TracerBuilder tracerBuilder) {
        if (tracerBuilder instanceof CapaTracerBuilder) {
            return (CapaTracerBuilder) tracerBuilder;
        }
        CapaTracerBuilder result = SpiUtils
                .loadFromSpiComponentFileNullable(CapaTracerBuilder.class, new Class[]{String.class, TracerBuilder.class},
                        new Object[]{tracerName, tracerBuilder}, FILE_SUFFIX, CACHE);
        if (result == null) {
            result = new CapaTracerBuilder(tracerName, tracerBuilder);
        }
        return result;
    }

}
