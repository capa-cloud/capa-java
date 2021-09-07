/*
 * Copyright (c) Microsoft Corporation and Capa Contributors.
 * Licensed under the MIT License.
 */

package group.rxcloud.capa.infrastructure.serializer;


import group.rxcloud.cloudruntimes.utils.TypeRef;

import java.io.IOException;

/**
 * Default serializer/deserializer for request/response objects.
 */
public class DefaultObjectSerializer extends ObjectSerializer implements CapaObjectSerializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] serialize(Object o) throws IOException {
        return super.serialize(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T deserialize(byte[] data, TypeRef<T> type) throws IOException {
        return super.deserialize(data, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType() {
        return "application/json";
    }
}
