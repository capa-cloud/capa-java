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
package group.rxcloud.capa.component.telemetry.trace.config;

import java.io.Serializable;

/**
 */
public class SpanLimitsConfig implements Serializable {

    private static final long serialVersionUID = 6918786580143929155L;

    private Integer maxNumAttributes;

    private Integer maxNumEvents;

    private Integer maxNumLinks;

    private Integer maxNumAttributesPerEvent;

    private Integer maxNumAttributesPerLink;

    private Integer maxAttributeValueLength;

    public Integer getMaxNumAttributes() {
        return maxNumAttributes;
    }

    public void setMaxNumAttributes(Integer maxNumAttributes) {
        this.maxNumAttributes = maxNumAttributes;
    }

    public Integer getMaxNumEvents() {
        return maxNumEvents;
    }

    public void setMaxNumEvents(Integer maxNumEvents) {
        this.maxNumEvents = maxNumEvents;
    }

    public Integer getMaxNumLinks() {
        return maxNumLinks;
    }

    public void setMaxNumLinks(Integer maxNumLinks) {
        this.maxNumLinks = maxNumLinks;
    }

    public Integer getMaxNumAttributesPerEvent() {
        return maxNumAttributesPerEvent;
    }

    public void setMaxNumAttributesPerEvent(Integer maxNumAttributesPerEvent) {
        this.maxNumAttributesPerEvent = maxNumAttributesPerEvent;
    }

    public Integer getMaxNumAttributesPerLink() {
        return maxNumAttributesPerLink;
    }

    public void setMaxNumAttributesPerLink(Integer maxNumAttributesPerLink) {
        this.maxNumAttributesPerLink = maxNumAttributesPerLink;
    }

    public Integer getMaxAttributeValueLength() {
        return maxAttributeValueLength;
    }

    public void setMaxAttributeValueLength(Integer maxAttributeValueLength) {
        this.maxAttributeValueLength = maxAttributeValueLength;
    }
}
