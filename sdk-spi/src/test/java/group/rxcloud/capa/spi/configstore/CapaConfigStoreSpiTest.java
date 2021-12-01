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
package group.rxcloud.capa.spi.configstore;

import com.google.common.collect.Lists;
import group.rxcloud.capa.component.configstore.ConfigurationItem;
import group.rxcloud.capa.component.configstore.GetRequest;
import group.rxcloud.capa.component.configstore.SubscribeReq;
import group.rxcloud.capa.component.configstore.SubscribeResp;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class CapaConfigStoreSpiTest {

    @Test
    public void testGet_Success() {
        TestCapaConfigStoreSpiImpl configStoreSpiImpl = new TestCapaConfigStoreSpiImpl(new DefaultObjectSerializer());
        Mono<List<ConfigurationItem<String>>> listMono = configStoreSpiImpl.get(constructGetRequest(), TypeRef.STRING);
        List<ConfigurationItem<String>> configurationItems = listMono.block();
        Assertions.assertNotNull(configurationItems);
        Assertions.assertEquals(2, configurationItems.size());

        ConfigurationItem<String> firstConfigurationItem = configurationItems.get(0);
        Assertions.assertEquals("testKey1", firstConfigurationItem.getKey());
        Assertions.assertNull(firstConfigurationItem.getContent());
        Assertions.assertEquals("testGroup", firstConfigurationItem.getGroup());
        Assertions.assertEquals("testLabel", firstConfigurationItem.getLabel());

        Assertions.assertNotNull(firstConfigurationItem.getLabel());
        Assertions.assertEquals(2, firstConfigurationItem.getTags().size());
        Assertions.assertEquals("default", firstConfigurationItem.getTags().get("cluster"));

        Assertions.assertNotNull(firstConfigurationItem.getMetadata());
        Assertions.assertEquals(2, firstConfigurationItem.getMetadata().size());
        Assertions.assertEquals("default", firstConfigurationItem.getMetadata().get("cluster"));
    }

    @Test
    public void testSubscribe_Success() {
        TestCapaConfigStoreSpiImpl configStoreSpiImpl = new TestCapaConfigStoreSpiImpl(new DefaultObjectSerializer());
        Flux<SubscribeResp<String>> subscribeFlux = configStoreSpiImpl.subscribe(constructSubscribeReq(), TypeRef.STRING);
        SubscribeResp<String> resp = subscribeFlux.blockFirst();
        Assertions.assertNotNull(resp);
        Assertions.assertEquals("12345", resp.getAppId());

        Assertions.assertEquals(2, resp.getItems().size());
        ConfigurationItem<String> firstConfigurationItem = resp.getItems().get(0);

        Assertions.assertEquals("testKey1", firstConfigurationItem.getKey());
        Assertions.assertNull(firstConfigurationItem.getContent());
        Assertions.assertEquals("testGroup", firstConfigurationItem.getGroup());
        Assertions.assertEquals("testLabel", firstConfigurationItem.getLabel());

        Assertions.assertNotNull(firstConfigurationItem.getLabel());
        Assertions.assertEquals(2, firstConfigurationItem.getTags().size());
        Assertions.assertEquals("default", firstConfigurationItem.getTags().get("cluster"));

        Assertions.assertNotNull(firstConfigurationItem.getMetadata());
        Assertions.assertEquals(2, firstConfigurationItem.getMetadata().size());
        Assertions.assertEquals("default", firstConfigurationItem.getMetadata().get("cluster"));
    }

    @Test
    public void testGetDefaultGroup_Success() {
        TestCapaConfigStoreSpiImpl configStoreSpiImpl = new TestCapaConfigStoreSpiImpl(new DefaultObjectSerializer());
        String defaultGroup = configStoreSpiImpl.getDefaultGroup();
        Assertions.assertEquals("application", defaultGroup);
    }

    @Test
    public void testGetDefaultLabel_Success() {
        TestCapaConfigStoreSpiImpl configStoreSpiImpl = new TestCapaConfigStoreSpiImpl(new DefaultObjectSerializer());
        String defaultLabel = configStoreSpiImpl.getDefaultLabel();
        Assertions.assertEquals("", defaultLabel);
    }

    private GetRequest constructGetRequest() {
        GetRequest getRequest = new GetRequest();
        getRequest.setAppId("12345");
        getRequest.setGroup("testGroup");
        getRequest.setLabel("testLabel");
        getRequest.setKeys(Lists.newArrayList("testKey1", "testKey2"));

        Map<String, String> metaDataMap = new HashMap<>();
        metaDataMap.put("cluster", "default");
        metaDataMap.put("namespace", "testNamespace");
        getRequest.setMetadata(metaDataMap);
        return getRequest;
    }

    private SubscribeReq constructSubscribeReq() {
        SubscribeReq req = new SubscribeReq();
        req.setAppId("12345");
        req.setGroup("testGroup");
        req.setLabel("testLabel");
        req.setKeys(Lists.newArrayList("testKey1", "testKey2"));

        Map<String, String> metaDataMap = new HashMap<>();
        metaDataMap.put("cluster", "default");
        metaDataMap.put("namespace", "testNamespace");
        req.setMetadata(metaDataMap);
        return req;
    }
}