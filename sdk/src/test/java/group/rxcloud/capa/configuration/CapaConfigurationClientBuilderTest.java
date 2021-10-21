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