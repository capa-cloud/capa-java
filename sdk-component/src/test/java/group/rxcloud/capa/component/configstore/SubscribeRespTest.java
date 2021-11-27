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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class SubscribeRespTest {

    @Test
    public void testSubscribeRespSetterGetter_Success() {
        SubscribeResp<String> resp = new SubscribeResp<>();
        resp.setAppId("12345");
        resp.setStoreName("qconfig");

        ConfigurationItem<String> configurationItem = new ConfigurationItem<>();
        resp.setItems(Lists.newArrayList(configurationItem));

        Assertions.assertNotNull(resp);
        Assertions.assertEquals("12345", resp.getAppId());
        Assertions.assertEquals("qconfig", resp.getStoreName());
        Assertions.assertEquals(1, resp.getItems().size());
    }
}