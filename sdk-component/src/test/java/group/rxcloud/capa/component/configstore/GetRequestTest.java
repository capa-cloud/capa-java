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
package group.rxcloud.capa.component.configstore;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class GetRequestTest {

    @Test
    public void testGetRequestSetterGetter_Success() {
        GetRequest getRequest = new GetRequest();
        getRequest.setAppId("12345");
        getRequest.setGroup("testGroup");
        getRequest.setLabel("testLabel");
        getRequest.setKeys(Lists.newArrayList("testKey1", "testKey2"));

        Map<String, String> metaDataMap = new HashMap<>();
        metaDataMap.put("cluster", "default");
        metaDataMap.put("namespace", "testNamespace");
        getRequest.setMetadata(metaDataMap);

        Assert.assertNotNull(getRequest);
        Assert.assertEquals("12345", getRequest.getAppId());
        Assert.assertEquals("testGroup", getRequest.getGroup());
        Assert.assertEquals("testLabel", getRequest.getLabel());

        Assert.assertEquals(2, getRequest.getKeys().size());
        Assert.assertEquals("testKey1", getRequest.getKeys().get(0));

        Assert.assertEquals(2, getRequest.getMetadata().size());
        Assert.assertEquals("testNamespace", getRequest.getMetadata().get("namespace"));
    }
}