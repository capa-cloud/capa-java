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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 23:00
 */
public class EventAttributeKeyTest {

    @Test
    public void getNum() {
        assertNull(EventAttributeKey.COUNT.getNum("aaa"));

        assertNull(EventAttributeKey.COUNT.getNum(Long.MAX_VALUE));
        assertNull(EventAttributeKey.COUNT.getNum(Long.MIN_VALUE));
        assertEquals(22, EventAttributeKey.COUNT.getNum(22L).intValue());
    }
}