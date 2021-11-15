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
package group.rxcloud.capa.infrastructure.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class CapaPropertiesTest {


    @Test
    public void testGetApiProtocol_Success() {
        String apiProtocol = CapaProperties.API_PROTOCOL.get();
        Assertions.assertEquals("HTTP", apiProtocol);
    }

    @Test
    public void testGetHttpClientReadTimeoutSeconds_Success() {
        Integer httpClientReadTimeoutSeconds = CapaProperties.HTTP_CLIENT_READ_TIMEOUT_SECONDS.get();
        Assertions.assertEquals(60, httpClientReadTimeoutSeconds.intValue());
    }

    @Test
    public void testGetComponentProperties_Success() {
        Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.apply("rpc");
        String value = properties.getProperty("key");
        Assertions.assertEquals("value", value);
    }
}
