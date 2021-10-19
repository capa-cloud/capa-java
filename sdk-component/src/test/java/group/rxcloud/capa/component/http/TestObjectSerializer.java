package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.ObjectSerializer;

/**
 * serializer/deserializer for request/response objects used in tests only
 */
public class TestObjectSerializer extends ObjectSerializer implements CapaObjectSerializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType() {
        return "";
    }
}
