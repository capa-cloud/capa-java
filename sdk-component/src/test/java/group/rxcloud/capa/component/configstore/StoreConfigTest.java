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

import java.util.Collections;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class StoreConfigTest {

    @Test
    public void testStoreConfigSetterGetter_Success() {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setStoreName("qconfig");
        storeConfig.setAddress(Lists.newArrayList("address"));
        storeConfig.setMetadata(Collections.emptyMap());
        storeConfig.setTimeOut("30000");

        Assertions.assertNotNull(storeConfig);
        Assertions.assertEquals("qconfig", storeConfig.getStoreName());

        Assertions.assertEquals(1, storeConfig.getAddress().size());
        Assertions.assertEquals("address", storeConfig.getAddress().get(0));

        Assertions.assertEquals(0, storeConfig.getMetadata().size());

        Assertions.assertEquals("30000", storeConfig.getTimeOut());
    }
}