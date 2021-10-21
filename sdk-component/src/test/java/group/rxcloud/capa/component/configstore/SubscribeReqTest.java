package group.rxcloud.capa.component.configstore;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class SubscribeReqTest {
    @Test
    public void testSubscribeReqSetterGetter_Success() {
        SubscribeReq req = new SubscribeReq();
        req.setAppId("12345");
        req.setGroup("testGroup");
        req.setLabel("testLabel");
        req.setKeys(Lists.newArrayList("testKey1", "testKey2"));
        req.setMetadata(Collections.emptyMap());

        Assert.assertNotNull(req);
        Assert.assertEquals("12345", req.getAppId());
        Assert.assertEquals("testGroup", req.getGroup());
        Assert.assertEquals("testLabel", req.getLabel());

        Assert.assertEquals(2, req.getKeys().size());
        Assert.assertEquals("testKey1", req.getKeys().get(0));

        Assert.assertEquals(0, req.getMetadata().size());
    }
}