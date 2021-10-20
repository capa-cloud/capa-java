package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.ObjectSerializer;
import org.junit.Assert;
import org.junit.Test;


public class CapaHttpBuilderTest {

    @Test
    public void testWithObjectSerializer_FailWhenCapaObjectSerializerIsNull() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CapaHttpBuilder().withObjectSerializer(null);
        });
    }

    @Test
    public void testWithObjectSerializer_FailWhenContentTypeIsNull() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new CapaHttpBuilder().withObjectSerializer(new TestObjectSerializer());
        });
    }

    @Test
    public void testWithObjectSerializer_SuccessWhenDefaultObjectSerializerIsUsed() {
        CapaHttpBuilder capaHttpBuilder = new CapaHttpBuilder().withObjectSerializer(new DefaultObjectSerializer());
        Assert.assertNotNull(capaHttpBuilder);
    }

    @Test
    public void testBuild_Success() {
        CapaHttpBuilder capaHttpBuilder = new CapaHttpBuilder();
        capaHttpBuilder.build();
    }

    /**
     * serializer/deserializer for request/response objects used in tests only
     */
    private class TestObjectSerializer extends ObjectSerializer implements CapaObjectSerializer {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getContentType() {
            return "";
        }
    }
}
