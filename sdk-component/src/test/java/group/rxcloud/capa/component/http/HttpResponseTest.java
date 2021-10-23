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

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseTest {

    @Test
    public void testGet_Success() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse<String> httpResponse = new HttpResponse<String>("body", headers, 200);

        Assert.assertEquals("body", httpResponse.getBody());

        Map<String, String> resultMap = headers;
        if (httpResponse.getHeaders() != null) {
            resultMap = httpResponse.getHeaders();
        }
        Assert.assertEquals("application/json", resultMap.get("Content-Type"));

        Assert.assertEquals(200, httpResponse.getStatusCode());
    }


}