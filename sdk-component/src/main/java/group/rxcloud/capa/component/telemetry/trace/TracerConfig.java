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

import java.io.Serializable;
import java.util.List;

/**
 * Trace config.
 */
public class TracerConfig implements Serializable {

    private static final long serialVersionUID = 6587103489345563395L;

    /**
     * Id generator class.
     */
    private String idGenerator;

    /**
     * Will trace/span id be validated with open telemetry restriction.
     */
    private boolean enableIdValidate;

    /**
     * Span limits.
     */
    private SpanLimitsConfig spanLimits;

    /**
     * Span processor classes.
     */
    private List<String> processors;

    public String getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(String className) {
        this.idGenerator = className;
    }

    public boolean isEnableIdValidate() {
        return enableIdValidate;
    }

    public void setEnableIdValidate(boolean enableIdValidate) {
        this.enableIdValidate = enableIdValidate;
    }

    public SpanLimitsConfig getSpanLimits() {
        return spanLimits;
    }

    public void setSpanLimits(SpanLimitsConfig spanLimits) {
        this.spanLimits = spanLimits;
    }

    public List<String> getProcessors() {
        return processors;
    }

    public void setProcessors(List<String> processors) {
        this.processors = processors;
    }

}
