package group.rxcloud.capa.component.configstore;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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