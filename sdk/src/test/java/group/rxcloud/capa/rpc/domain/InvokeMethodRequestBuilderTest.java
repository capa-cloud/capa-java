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
package group.rxcloud.capa.rpc.domain;

import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class InvokeMethodRequestBuilderTest {

    private InvokeMethodRequestBuilder invokeMethodRequestBuilder;

    @BeforeEach
    public void setUp() {
        invokeMethodRequestBuilder = new InvokeMethodRequestBuilder("appId", "method");
    }

    @Test
    public void testWithContentType_Success() {
        InvokeMethodRequestBuilder requestBuilder = invokeMethodRequestBuilder.withContentType("application/json");
        Assertions.assertNotNull(requestBuilder);
    }

    @Test
    public void testWithBody_Success() {
        InvokeMethodRequestBuilder requestBuilder = invokeMethodRequestBuilder.withBody("body");
        Assertions.assertNotNull(requestBuilder);
    }

    @Test
    public void testWithHttpExtension_Success() {
        InvokeMethodRequestBuilder requestBuilder = invokeMethodRequestBuilder.withHttpExtension(HttpExtension.POST);
        Assertions.assertNotNull(requestBuilder);
    }

    @Test
    public void testWithMetadata_Success() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        InvokeMethodRequestBuilder requestBuilder = invokeMethodRequestBuilder.withMetadata(metadata);
        Assertions.assertNotNull(requestBuilder);
    }

    @Test
    public void testBuild_Success() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        InvokeMethodRequest invokeMethodRequest = invokeMethodRequestBuilder
                .withContentType("application/json")
                .withBody("body")
                .withHttpExtension(HttpExtension.POST)
                .withMetadata(metadata)
                .build();

        Assertions.assertEquals("appId", invokeMethodRequest.getAppId());
        Assertions.assertEquals("method", invokeMethodRequest.getMethod());
        Assertions.assertEquals("application/json", invokeMethodRequest.getContentType());
        Assertions.assertEquals("body", invokeMethodRequest.getBody());
        Assertions.assertEquals(HttpExtension.POST, invokeMethodRequest.getHttpExtension());

        Map<String, String> requestMetadata = invokeMethodRequest.getMetadata();
        Assertions.assertEquals("value", requestMetadata.get("key"));
    }
}
