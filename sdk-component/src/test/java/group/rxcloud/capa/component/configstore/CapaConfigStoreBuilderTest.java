package group.rxcloud.capa.component.configstore;

import com.google.common.collect.Lists;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class CapaConfigStoreBuilderTest {

    @Test
    public void testWithObjectSerializer_FailWhenCapaObjectSerializerIsNull() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CapaConfigStoreBuilder(constructStoreConfig()).withObjectSerializer(null);
        });
    }

    @Test
    public void testWithObjectSerializer_FailWhenContentTypeIsNull() {
        IllegalArgumentException illegalArgumentException = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CapaConfigStoreBuilder(constructStoreConfig()).withObjectSerializer(new TestObjectSerializer());
        });
    }

    @Test
    public void testWithObjectSerializer_SuccessWhenDefaultObjectSerializerIsUsed() {
        CapaConfigStoreBuilder capaConfigStoreBuilder = new CapaConfigStoreBuilder(constructStoreConfig()).withObjectSerializer(new DefaultObjectSerializer());
        Assert.assertNotNull(capaConfigStoreBuilder);
    }

    @Test
    public void testBuild_Success() {
        CapaConfigStoreBuilder capaConfigStoreBuilder = new CapaConfigStoreBuilder(constructStoreConfig());
        CapaConfigStore build = capaConfigStoreBuilder.build();
        Assert.assertNotNull(build);
    }

    private StoreConfig constructStoreConfig() {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setStoreName("qconfig");
        storeConfig.setAddress(Lists.newArrayList("address"));
        storeConfig.setMetadata(Collections.emptyMap());
        storeConfig.setTimeOut("30000");
        return storeConfig;
    }
}