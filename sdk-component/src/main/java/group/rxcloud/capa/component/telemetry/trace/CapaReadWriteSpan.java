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
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.data.SpanData;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Read write span proxy.
 */
public class CapaReadWriteSpan implements ReadWriteSpan {

    protected final ReadWriteSpan span;

    protected final String tracerName;

    protected final String version;

    protected final String schemaUrl;

    public CapaReadWriteSpan(String tracerName, String version, String schemaUrl, ReadWriteSpan span) {
        this.span = span;
        this.tracerName = tracerName;
        this.version = version;
        this.schemaUrl = schemaUrl;
    }

    public String getTracerName() {
        return tracerName == null ? getInstrumentationLibraryInfo().getName() : tracerName;
    }

    public String getVersion() {
        return version == null ? getInstrumentationLibraryInfo().getVersion() : version;
    }

    public String getSchemaUrl() {
        return schemaUrl == null ? getInstrumentationLibraryInfo().getSchemaUrl() : schemaUrl;
    }

    @Override
    public <T> Span setAttribute(AttributeKey<T> key, T value) {
        span.setAttribute(key, value);
        return this;
    }

    @Override
    public Span addEvent(String name, Attributes attributes) {
        span.addEvent(name, attributes);
        return this;
    }

    @Override
    public Span addEvent(String name, Attributes attributes, long timestamp, TimeUnit unit) {
        span.addEvent(name, attributes, timestamp, unit);
        return this;
    }

    @Override
    public Span setStatus(StatusCode statusCode, String description) {
        span.setStatus(statusCode, description);
        return this;
    }

    @Override
    public Span recordException(Throwable exception, Attributes additionalAttributes) {
        span.recordException(exception, additionalAttributes);
        return this;
    }

    @Override
    public Span updateName(String name) {
        span.updateName(name);
        return this;
    }

    @Override
    public void end() {
        span.end();
    }

    @Override
    public void end(long timestamp, TimeUnit unit) {
        span.end(timestamp, unit);
    }

    @Override
    public SpanContext getSpanContext() {
        return span.getSpanContext();
    }

    @Override
    public SpanContext getParentSpanContext() {
        return span.getParentSpanContext();
    }

    @Override
    public String getName() {
        return span.getName();
    }

    @Override
    public SpanData toSpanData() {
        return span.toSpanData();
    }

    @Override
    public InstrumentationLibraryInfo getInstrumentationLibraryInfo() {
        return span.getInstrumentationLibraryInfo();
    }

    @Override
    public boolean hasEnded() {
        return span.hasEnded();
    }

    @Override
    public long getLatencyNanos() {
        return span.getLatencyNanos();
    }

    @Override
    public SpanKind getKind() {
        return span.getKind();
    }

    @Nullable
    @Override
    public <T> T getAttribute(AttributeKey<T> key) {
        return span.getAttribute(key);
    }

    @Override
    public boolean isRecording() {
        return span.isRecording();
    }
}
