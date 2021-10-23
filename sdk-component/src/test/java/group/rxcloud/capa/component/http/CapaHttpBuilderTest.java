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
