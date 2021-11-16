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

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerBuilder;

/**
 */
public class CapaTracerBuilder implements TracerBuilder {

    protected final TracerBuilder tracerBuilder;

    protected final String tracerName;

    protected String version;

    protected String schemaUrl;

    public CapaTracerBuilder(String tracerName, TracerBuilder tracerBuilder) {
        this.tracerName = tracerName;
        this.tracerBuilder = tracerBuilder;
    }

    @Override
    public TracerBuilder setSchemaUrl(String schemaUrl) {
        tracerBuilder.setSchemaUrl(schemaUrl);
        this.schemaUrl = schemaUrl;
        return this;
    }

    @Override
    public TracerBuilder setInstrumentationVersion(String instrumentationVersion) {
        tracerBuilder.setInstrumentationVersion(instrumentationVersion);
        this.version = instrumentationVersion;
        return this;
    }

    @Override
    public Tracer build() {
        return CapaWrapper.wrap(tracerName, version, schemaUrl, tracerBuilder.build());
    }
}
