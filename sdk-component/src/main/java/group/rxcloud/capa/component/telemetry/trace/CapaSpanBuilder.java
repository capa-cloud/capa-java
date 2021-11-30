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

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;

import java.util.concurrent.TimeUnit;

/**
 * Span builder proxy.
 */
public class CapaSpanBuilder implements SpanBuilder {

    protected final String tracerName;

    protected String version;

    protected String schemaUrl;

    protected final String spanName;

    protected final SpanBuilder spanBuilder;

    public CapaSpanBuilder(String tracerName, String version, String schemaUrl, String spanName,
                           SpanBuilder spanBuilder) {
        this.tracerName = tracerName;
        this.version = version;
        this.schemaUrl = schemaUrl;
        this.spanName = spanName;
        this.spanBuilder = spanBuilder;
    }

    public String getSchemaUrl() {
        return schemaUrl;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public SpanBuilder setParent(Context context) {
        spanBuilder.setParent(context);
        return this;
    }

    @Override
    public SpanBuilder setNoParent() {
        spanBuilder.setNoParent();
        return this;
    }

    @Override
    public SpanBuilder addLink(SpanContext spanContext) {
        spanBuilder.addLink(spanContext);
        return this;
    }

    @Override
    public SpanBuilder addLink(SpanContext spanContext, Attributes attributes) {
        spanBuilder.addLink(spanContext, attributes);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, String value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, long value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, double value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setAttribute(String key, boolean value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public <T> SpanBuilder setAttribute(AttributeKey<T> key, T value) {
        spanBuilder.setAttribute(key, value);
        return this;
    }

    @Override
    public SpanBuilder setSpanKind(SpanKind spanKind) {
        spanBuilder.setSpanKind(spanKind);
        return this;
    }

    @Override
    public SpanBuilder setStartTimestamp(long startTimestamp, TimeUnit unit) {
        spanBuilder.setStartTimestamp(startTimestamp, unit);
        return this;
    }

    @Override
    public Span startSpan() {
        Span span = spanBuilder.startSpan();
        if (span instanceof ReadWriteSpan) {
            return CapaWrapper.wrap(tracerName, version, schemaUrl, (ReadWriteSpan) span);
        }
        return span;
    }
}
