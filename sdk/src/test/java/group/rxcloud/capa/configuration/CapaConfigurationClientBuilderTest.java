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
package group.rxcloud.capa.configuration;

import com.google.common.collect.Lists;
import group.rxcloud.capa.component.configstore.CapaConfigStore;
import group.rxcloud.capa.component.configstore.CapaConfigStoreBuilder;
import group.rxcloud.capa.component.configstore.StoreConfig;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Reckless Xu
 * @date 2021/10/21
 */
public class CapaConfigurationClientBuilderTest {

    @Test
    public void testBuild_WithCapaConfigStoreBuilderListAsParametersConstructor() {
        CapaConfigStoreBuilder storeBuilder = Mockito.mock(CapaConfigStoreBuilder.class);
        CapaConfigStore configStore = Mockito.mock(CapaConfigStore.class);
        Mockito.when(storeBuilder.build()).thenReturn(configStore);
        CapaConfigurationClientBuilder builder = new CapaConfigurationClientBuilder(Lists.newArrayList(storeBuilder));
        Assert.assertNotNull(builder.build());
    }

    @Test
    public void testBuild_WithCapaConfigStoreBuilderSuppierAsParametersConstructor() {
        CapaConfigStoreBuilder storeBuilder = Mockito.mock(CapaConfigStoreBuilder.class);
        CapaConfigStore configStore = Mockito.mock(CapaConfigStore.class);
        Mockito.when(storeBuilder.build()).thenReturn(configStore);
        CapaConfigurationClientBuilder builder = new CapaConfigurationClientBuilder(() -> storeBuilder);
        Assert.assertNotNull(builder.build());
    }


    @Test
    public void testConstructorWithStoreConfigAsParameter() {
        CapaConfigurationClientBuilder builder = new CapaConfigurationClientBuilder(new StoreConfig());
        Assert.assertNotNull(builder);
    }
}