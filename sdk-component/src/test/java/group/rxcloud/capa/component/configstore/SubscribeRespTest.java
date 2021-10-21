package group.rxcloud.capa.component.configstore;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

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

        Assert.assertNotNull(resp);
        Assert.assertEquals("12345", resp.getAppId());
        Assert.assertEquals("qconfig", resp.getStoreName());
        Assert.assertEquals(1, resp.getItems().size());
    }
}