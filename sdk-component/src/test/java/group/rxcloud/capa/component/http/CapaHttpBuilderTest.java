package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import org.junit.Assert;
import org.junit.Test;


public class CapaHttpBuilderTest {

    @Test
    public void testWithObjectSerializer_FailWhenCapaObjectSerializerIsNull() {
        IllegalArgumentException illegalArgumentException = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CapaHttpBuilder().withObjectSerializer(null);
        });
    }

    @Test
    public void testWithObjectSerializer_FailWhenContentTypeIsNull() {
        IllegalArgumentException illegalArgumentException = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CapaHttpBuilder().withObjectSerializer(new TestObjectSerializer());
        });
    }

    @Test
    public void testWithObjectSerializer_SuccessWhenDefaultObjectSerializerIsUsed() {
        CapaHttpBuilder capaHttpBuilder = new CapaHttpBuilder().withObjectSerializer(new DefaultObjectSerializer());
        Assert.assertNotNull(capaHttpBuilder);
    }

    @Test   // TODO: 2021/10/18   need add test
    public void testBuild_Success() {

    }

    @Test // TODO: 2021/10/18   need add test
    public void testBuild_FailWhenException() {

    }


}
