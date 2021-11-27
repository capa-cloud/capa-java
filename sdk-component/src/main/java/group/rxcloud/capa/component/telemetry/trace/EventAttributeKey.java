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
 * Attribute key for event params.
 */
public final class EventAttributeKey implements AttributeKey<Long> {

    public static EventAttributeKey SIZE = new EventAttributeKey(AttributeKey.longKey("_capa_event_size"));

    public static EventAttributeKey COUNT = new EventAttributeKey(AttributeKey.longKey("_capa_event_count"));

    public static EventAttributeKey FAIL = new EventAttributeKey(AttributeKey.longKey("_capa_event_fail"));

    private final AttributeKey<Long> key;

    private EventAttributeKey(AttributeKey<Long> key) {
        this.key = key;
    }

    public Integer getNum(Object value) {
        if (!(value instanceof Long)) {
            return null;
        }

        Long l = (Long) value;
        // overflow
        if (l.compareTo((long)Integer.MAX_VALUE) > 0 || l.compareTo((long)Integer.MIN_VALUE) < 0 ) {
            return null;
        }

        return ((Long) value).intValue();
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
