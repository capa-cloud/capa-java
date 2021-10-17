/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package group.rxcloud.capa.infrastructure.serializer;


import group.rxcloud.cloudruntimes.utils.TypeRef;

import java.io.IOException;

/**
 * Serializes and deserializes application's objects.
 */
public interface CapaObjectSerializer {

    /**
     * Serializes the given object as byte[].
     *
     * @param o Object to be serialized.
     * @return Serialized object.
     * @throws IOException If cannot serialize.
     */
    byte[] serialize(Object o) throws IOException;

    /**
     * Deserializes the given byte[] into a object.
     *
     * @param data Data to be deserialized.
     * @param type Type of object to be deserialized.
     * @param <T>  Type of object to be deserialized.
     * @return Deserialized object.
     * @throws IOException If cannot deserialize object.
     */
    <T> T deserialize(byte[] data, TypeRef<T> type) throws IOException;

    /**
     * Returns the content type of the request.
     *
     * @return content type of the request
     */
    String getContentType();
}
