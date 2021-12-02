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
import io.opentelemetry.api.common.AttributeType;

/**
 * Attribute key for status params.
 */
public final class StatusAttributeKey implements AttributeKey<String> {

    public static StatusAttributeKey MESSAGE = new StatusAttributeKey(AttributeKey.stringKey("_capa_error_message"));

    public static StatusAttributeKey STATUS = new StatusAttributeKey(AttributeKey.stringKey("_capa_status"));

    public static StatusAttributeKey LINK = new StatusAttributeKey(AttributeKey.stringKey("_capa_link"));

    private final AttributeKey<String> key;

    private StatusAttributeKey(AttributeKey<String> key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key.getKey();
    }

    @Override
    public AttributeType getType() {
        return key.getType();
    }
}
