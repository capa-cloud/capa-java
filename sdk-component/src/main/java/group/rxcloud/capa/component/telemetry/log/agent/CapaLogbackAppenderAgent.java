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

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import group.rxcloud.capa.infrastructure.config.CapaProperties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * The agent of the logback impl.
 */
public class CapaLogbackAppenderAgent<EVENT> extends UnsynchronizedAppenderBase<EVENT> {

    /**
     * The log component type.
     */
    private static final String LOG_COMPONENT_TYPE = "log";
    /**
     * Capa logback appender instance.
     */
    private static final CapaLogbackAppender logbackAppender;

    /**
     * Init the logbackAppender impl.
     */
    static {
        logbackAppender = buildCapaLogbackAppender();
    }

    /**
     * Build the logback appender impl.
     *
     * @return CapaLogbackAppender instance.
     */
    public static CapaLogbackAppender buildCapaLogbackAppender() {
        // load spi capa Log4j appender impl
        try {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply(LOG_COMPONENT_TYPE);
            String capaLogbackAppenderClassPath = properties.getProperty(CapaLogbackAppender.class.getName());
            Class<? extends CapaLogbackAppender> aClass = (Class<? extends CapaLogbackAppender>) Class.forName(capaLogbackAppenderClassPath);
            Constructor<? extends CapaLogbackAppender> constructor = aClass.getConstructor();
            Object newInstance = constructor.newInstance();
            return (CapaLogbackAppender) newInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("No Capa Logback Appender supported.");
        }
    }

    /**
     * Deal with the log.
     *
     * @param event The log event.
     */
    @Override
    protected void append(EVENT event) {
        logbackAppender.appendLog(event);
    }

    /**
     * The abstract api of the logback appender impl.Implement this and provide your specific impl.
     */
    public interface CapaLogbackAppender<EVENT> {

        /**
         * Deal with the log.
         *
         * @param event The log event.
         */
        void appendLog(EVENT event);
    }
}
