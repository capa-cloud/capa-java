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
package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huijing xu
 * @date 2021/10/18
 */
public class CapaHttpTest {

    @Test
    public void testInvokeApi_Success() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TestCapaHttp capaHttp = new TestCapaHttp(builder.build(), new DefaultObjectSerializer(), null, null);
        Mono<HttpResponse<String>> responseMono = capaHttp.invokeApi("post",
                null,
                null,
                null,
                headers,
                null,
                TypeRef.STRING);

        HttpResponse<String> block = responseMono.block();
        int statusCode = block.getStatusCode();

        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void testClose_Success() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TestCapaHttp capaHttp = new TestCapaHttp(builder.build(), new DefaultObjectSerializer(), null, null);

        capaHttp.close();
    }
}
