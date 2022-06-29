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

import group.rxcloud.capa.infrastructure.plugin.PluginLoader;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerBuilder;
import io.opentelemetry.sdk.trace.ReadWriteSpan;

/**
 * Load capa implementation.
 */
public interface CapaTracerWrapperPlugin {

    /**
     * Load plugin capa tracer wrapper plugin.
     *
     * @return the capa tracer wrapper plugin
     */
    static CapaTracerWrapperPlugin loadPlugin() {
        return PluginLoader.loadPluginImpl(CapaTracerWrapperPlugin.class, CapaTracerWrapperPlugin.CapaTracerWrapperDefault::new);
    }

    /**
     * Wrap capa span builder.
     *
     * @param tracerName the tracer name
     * @param version    the version
     * @param schemaUrl  the schema url
     * @param spanName   the span name
     * @param builder    the builder
     * @return the capa span builder
     */
    CapaSpanBuilder wrap(String tracerName, String version, String schemaUrl, String spanName, SpanBuilder builder);

    /**
     * Wrap capa read write span.
     *
     * @param tracerName the tracer name
     * @param version    the version
     * @param schemaUrl  the schema url
     * @param span       the span
     * @return the capa read write span
     */
    CapaReadWriteSpan wrap(String tracerName, String version, String schemaUrl, ReadWriteSpan span);

    /**
     * Wrap capa tracer.
     *
     * @param tracerName the tracer name
     * @param version    the version
     * @param schemaUrl  the schema url
     * @param tracer     the tracer
     * @return the capa tracer
     */
    CapaTracer wrap(String tracerName, String version, String schemaUrl, Tracer tracer);

    /**
     * Wrap capa tracer builder.
     *
     * @param tracerName    the tracer name
     * @param tracerBuilder the tracer builder
     * @return the capa tracer builder
     */
    CapaTracerBuilder wrap(String tracerName, TracerBuilder tracerBuilder);

    /**
     * The Capa tracer wrapper default.
     */
    class CapaTracerWrapperDefault implements CapaTracerWrapperPlugin {

        @Override
        public CapaSpanBuilder wrap(String tracerName, String version, String schemaUrl, String spanName, SpanBuilder builder) {
            if (builder instanceof CapaSpanBuilder) {
                return (CapaSpanBuilder) builder;
            }
            return new CapaSpanBuilder(tracerName, version, schemaUrl, spanName, builder);
        }

        @Override
        public CapaReadWriteSpan wrap(String tracerName, String version, String schemaUrl, ReadWriteSpan span) {
            if (span instanceof CapaReadWriteSpan) {
                return (CapaReadWriteSpan) span;
            }
            return new CapaReadWriteSpan(tracerName, version, schemaUrl, span);
        }

        @Override
        public CapaTracer wrap(String tracerName, String version, String schemaUrl, Tracer tracer) {
            if (tracer instanceof CapaTracer) {
                return (CapaTracer) tracer;
            }
            return new CapaTracer(tracerName, version, schemaUrl, tracer);
        }

        @Override
        public CapaTracerBuilder wrap(String tracerName, TracerBuilder tracerBuilder) {
            if (tracerBuilder instanceof CapaTracerBuilder) {
                return (CapaTracerBuilder) tracerBuilder;
            }
            return new CapaTracerBuilder(tracerName, tracerBuilder);
        }
    }
}
