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
package group.rxcloud.capa.component.telemetry.log.agent;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CapaLog4jAppenderAgentTest {

    @Test
    void testCreateAppender() {
        Filter filter = Mockito.mock(Filter.class);
        Layout layout = Mockito.mock(Layout.class);
        new CapaLog4jAppenderAgent("CapaLog4jAppender", filter, layout, true);
    }

    @Test
    void testBuildCapaLog4jAppender() {
        Filter filter = Mockito.mock(Filter.class);
        Layout layout = Mockito.mock(Layout.class);
        CapaLog4jAppenderAgent.createAppender("CapaLog4jAppender", filter, layout, true);
    }

    @Test
    void testAppend() {
        Filter filter = Mockito.mock(Filter.class);
        Layout layout = Mockito.mock(Layout.class);
        CapaLog4jAppenderAgent capaLog4jAppender = CapaLog4jAppenderAgent.createAppender("CapaLog4jAppender", filter, layout, true);
        LogEvent logEvent = Mockito.mock(LogEvent.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(logEvent.getMessage()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(message.getFormattedMessage()).thenReturn("TEST");
        capaLog4jAppender.append(logEvent);
    }
}