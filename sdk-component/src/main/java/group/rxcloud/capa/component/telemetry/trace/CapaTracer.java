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

import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;

/**
 * Tracer proxy.
 */
public class CapaTracer implements Tracer {

    protected final String tracerName;
    protected final String version;
    protected final String schemaUrl;
    protected final Tracer tracer;

    public CapaTracer(String tracerName, String version, String schemaUrl, Tracer tracer) {
        this.tracerName = tracerName;
        this.version = version;
        this.schemaUrl = schemaUrl;
        this.tracer = tracer;
    }

    @Override
    public SpanBuilder spanBuilder(String spanName) {
        SpanBuilder builder = tracer.spanBuilder(spanName);
        return CapaTracerWrapper.wrap(tracerName, version, schemaUrl, spanName, builder);
    }

    public String getVersion() {
        return version;
    }

    public String getSchemaUrl() {
        return schemaUrl;
    }
}
