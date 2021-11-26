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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

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
        LogEvent logEvent = new LogEvent() {
            @Override
            public LogEvent toImmutable() {
                return null;
            }

            @Override
            public Map<String, String> getContextMap() {
                return null;
            }

            @Override
            public ReadOnlyStringMap getContextData() {
                return null;
            }

            @Override
            public ThreadContext.ContextStack getContextStack() {
                return null;
            }

            @Override
            public String getLoggerFqcn() {
                return null;
            }

            @Override
            public Level getLevel() {
                return null;
            }

            @Override
            public String getLoggerName() {
                return null;
            }

            @Override
            public Marker getMarker() {
                return null;
            }

            @Override
            public Message getMessage() {
                return new Message() {
                    @Override
                    public String getFormattedMessage() {
                        return "TEST";
                    }

                    @Override
                    public String getFormat() {
                        return null;
                    }

                    @Override
                    public Object[] getParameters() {
                        return new Object[0];
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }
                };
            }

            @Override
            public long getTimeMillis() {
                return 0;
            }

            @Override
            public StackTraceElement getSource() {
                return null;
            }

            @Override
            public String getThreadName() {
                return null;
            }

            @Override
            public long getThreadId() {
                return 0;
            }

            @Override
            public int getThreadPriority() {
                return 0;
            }

            @Override
            public Throwable getThrown() {
                return null;
            }

            @Override
            public ThrowableProxy getThrownProxy() {
                return null;
            }

            @Override
            public boolean isEndOfBatch() {
                return false;
            }

            @Override
            public boolean isIncludeLocation() {
                return false;
            }

            @Override
            public void setEndOfBatch(boolean endOfBatch) {

            }

            @Override
            public void setIncludeLocation(boolean locationRequired) {

            }

            @Override
            public long getNanoTime() {
                return 0;
            }
        };
        capaLog4jAppender.append(logEvent);
    }
}