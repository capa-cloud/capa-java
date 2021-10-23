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
import group.rxcloud.capa.component.configstore.GetRequest;
import group.rxcloud.capa.component.configstore.SubscribeReq;
import group.rxcloud.capa.component.configstore.SubscribeResp;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SaveConfigurationRequest;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class CapaConfigurationClientStoreTest {

    @Test
    public void testSaveConfiguration_FailWhenThrowUnsupportedOperationException() {
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(Collections.emptyList());
        Assert.assertThrows(UnsupportedOperationException.class, () -> clientStore.saveConfiguration(new SaveConfigurationRequest()).block());
    }

    @Test
    public void testDeleteConfiguration_FailWhenThrowUnsupportedOperationException() {
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(Collections.emptyList());
        Assert.assertThrows(UnsupportedOperationException.class, () -> clientStore.deleteConfiguration(new ConfigurationRequestItem()).block());

    }

    @Test
    public void testShutDown_Success() {
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(Collections.emptyList());
        Assert.assertEquals(Mono.empty().block(), clientStore.shutdown().block());
    }

    @Test
    public void testGetConfiguration_FailWhenStoreNameIsNull() {
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(Collections.emptyList());
        Assert.assertThrows(IllegalArgumentException.class, () -> clientStore.getConfiguration(new ConfigurationRequestItem(), TypeRef.STRING).block());
    }

    @Test
    public void testGetConfiguration_SuccessWithConfigurationHasValue() {
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(constructConfigStoreListWithConfirgurationItemHasValue());
        Mono<List<ConfigurationItem<String>>> configuration = clientStore.getConfiguration(constructConfigurationRequestItem(), TypeRef.STRING);
        List<ConfigurationItem<String>> configurationItems = configuration.block();
        Assert.assertNotNull(configurationItems);
        Assert.assertEquals(1, configurationItems.size());

        ConfigurationItem<String> firstItem = configurationItems.get(0);
        Assert.assertEquals("testKey1", firstItem.getKey());
        Assert.assertEquals("testContent", firstItem.getContent());
        Assert.assertEquals("testGroup", firstItem.getGroup());
        Assert.assertEquals("testLabel", firstItem.getLabel());
    }

    @Test
    public void testGetConfiguration_SuccessWithConfigurationItemIsEmpty() {
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(constructConfigStoreListWithConfigurationItemIsEmpty());
        Mono<List<ConfigurationItem<String>>> configuration = clientStore.getConfiguration(constructConfigurationRequestItem(), TypeRef.STRING);
        List<ConfigurationItem<String>> configurationItems = configuration.block();
        Assert.assertNull(configurationItems);
    }

    @Test
    public void testSubscribeConfiguration_SuccessWithSubscribeRespHasValue() {
        CapaConfigStore mockConfigStore = Mockito.mock(CapaConfigStore.class);
        Mockito.when(mockConfigStore.getStoreName()).thenReturn("qconfig");

        SubscribeResp<String> subscribeResp = new SubscribeResp<>();
        List<group.rxcloud.capa.component.configstore.ConfigurationItem<String>> result = new ArrayList<>();
        group.rxcloud.capa.component.configstore.ConfigurationItem<String> configurationItem = new group.rxcloud.capa.component.configstore.ConfigurationItem();
        configurationItem.setKey("testKey1");
        configurationItem.setContent("testContent");
        configurationItem.setGroup("testGroup");
        configurationItem.setLabel("testLabel");
        result.add(configurationItem);
        subscribeResp.setItems(result);
        Mockito.when(mockConfigStore.subscribe(any(SubscribeReq.class), any(TypeRef.class))).thenReturn(Flux.just(subscribeResp));

        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(Lists.newArrayList(mockConfigStore));

        ConfigurationRequestItem configurationRequestItem = new ConfigurationRequestItem();
        configurationRequestItem.setStoreName("qconfig");
        Flux<SubConfigurationResp<String>> subConfigurationRespFlux = clientStore.subscribeConfiguration(configurationRequestItem, TypeRef.STRING);
        SubConfigurationResp<String> stringSubConfigurationResp = subConfigurationRespFlux.blockFirst();
        Assert.assertNotNull(stringSubConfigurationResp);
        Assert.assertEquals(1, stringSubConfigurationResp.getItems().size());
        ConfigurationItem<String> firstItem = stringSubConfigurationResp.getItems().get(0);
        Assert.assertEquals("testKey1", firstItem.getKey());
        Assert.assertEquals("testContent", firstItem.getContent());
        Assert.assertEquals("testGroup", firstItem.getGroup());
        Assert.assertEquals("testLabel", firstItem.getLabel());
    }

    @Test
    public void testClose_Success() throws Exception {
        CapaConfigStore mockConfigStore = Mockito.mock(CapaConfigStore.class);
        Mockito.doNothing().when(mockConfigStore).close();
        CapaConfigurationClientStore clientStore = new CapaConfigurationClientStore(Lists.newArrayList(mockConfigStore));
        clientStore.close();
        Mockito.verify(mockConfigStore).close();
    }

    private List<CapaConfigStore> constructConfigStoreListWithConfirgurationItemHasValue() {
        CapaConfigStore mockConfigStore = Mockito.mock(CapaConfigStore.class);
        Mockito.when(mockConfigStore.getStoreName()).thenReturn("qconfig");

        List<group.rxcloud.capa.component.configstore.ConfigurationItem<String>> result = new ArrayList<>();
        group.rxcloud.capa.component.configstore.ConfigurationItem<String> configurationItem = new group.rxcloud.capa.component.configstore.ConfigurationItem();
        configurationItem.setKey("testKey1");
        configurationItem.setContent("testContent");
        configurationItem.setGroup("testGroup");
        configurationItem.setLabel("testLabel");
        result.add(configurationItem);
        Mono<List<group.rxcloud.capa.component.configstore.ConfigurationItem<String>>> just = Mono.just(result);
        Mockito.when(mockConfigStore.get(any(GetRequest.class), any(TypeRef.class))).thenReturn(just);
        return Lists.newArrayList(mockConfigStore);
    }

    private List<CapaConfigStore> constructConfigStoreListWithConfigurationItemIsEmpty() {
        CapaConfigStore mockConfigStore = Mockito.mock(CapaConfigStore.class);
        Mockito.when(mockConfigStore.getStoreName()).thenReturn("qconfig");

        List<group.rxcloud.capa.component.configstore.ConfigurationItem> result = new ArrayList<>();
        Mono<List<group.rxcloud.capa.component.configstore.ConfigurationItem>> just = Mono.just(result);
        Mockito.when(mockConfigStore.get(any(GetRequest.class), any(TypeRef.class))).thenReturn(just);
        return Lists.newArrayList(mockConfigStore);
    }

    private ConfigurationRequestItem constructConfigurationRequestItem() {
        ConfigurationRequestItem item = new ConfigurationRequestItem();
        item.setStoreName("qconfig");
        return item;
    }
}