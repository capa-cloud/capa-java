package group.rxcloud.capa.component.configstore;
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
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class ConfigurationItemTest {

    @Test
    public void testConfigurationItemSetterGetter_Success() {
        ConfigurationItem<String> configurationItem = new ConfigurationItem<>();
        configurationItem.setKey("testKey");
        configurationItem.setContent("testContent");
        configurationItem.setGroup("testGroup");
        configurationItem.setLabel("testLabel");

        Map<String, String> metaDataMap = new HashMap<>();
        metaDataMap.put("cluster", "default");
        metaDataMap.put("namespace", "testNamespace");
        configurationItem.setMetadata(metaDataMap);
        configurationItem.setTags(metaDataMap);

        Assert.assertNotNull(configurationItem);
        Assert.assertEquals("testKey", configurationItem.getKey());
        Assert.assertEquals("testContent", configurationItem.getContent());
        Assert.assertEquals("testGroup", configurationItem.getGroup());
        Assert.assertEquals("testLabel", configurationItem.getLabel());

        Assert.assertEquals(2, configurationItem.getMetadata().size());
        Assert.assertEquals("default", configurationItem.getMetadata().get("cluster"));

        Assert.assertEquals(2, configurationItem.getTags().size());
        Assert.assertEquals("testNamespace", configurationItem.getTags().get("namespace"));
    }
}