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

import java.util.Collections;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class SubscribeReqTest {
    @Test
    public void testSubscribeReqSetterGetter_Success() {
        SubscribeReq req = new SubscribeReq();
        req.setAppId("12345");
        req.setGroup("testGroup");
        req.setLabel("testLabel");
        req.setKeys(Lists.newArrayList("testKey1", "testKey2"));
        req.setMetadata(Collections.emptyMap());

        Assert.assertNotNull(req);
        Assert.assertEquals("12345", req.getAppId());
        Assert.assertEquals("testGroup", req.getGroup());
        Assert.assertEquals("testLabel", req.getLabel());

        Assert.assertEquals(2, req.getKeys().size());
        Assert.assertEquals("testKey1", req.getKeys().get(0));

        Assert.assertEquals(0, req.getMetadata().size());
    }
}