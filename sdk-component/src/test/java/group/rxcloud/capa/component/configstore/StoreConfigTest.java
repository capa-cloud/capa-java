package group.rxcloud.capa.component.configstore;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

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

        Assert.assertNotNull(storeConfig);
        Assert.assertEquals("qconfig", storeConfig.getStoreName());

        Assert.assertEquals(1, storeConfig.getAddress().size());
        Assert.assertEquals("address", storeConfig.getAddress().get(0));

        Assert.assertEquals(0, storeConfig.getMetadata().size());

        Assert.assertEquals("30000", storeConfig.getTimeOut());
    }

}