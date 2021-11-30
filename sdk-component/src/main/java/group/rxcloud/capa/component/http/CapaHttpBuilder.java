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


import group.rxcloud.capa.infrastructure.CapaClassLoader;
import group.rxcloud.capa.infrastructure.CapaProperties;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import okhttp3.OkHttpClient;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A builder for the {@link CapaHttp} implementor.
 */
public class CapaHttpBuilder {

    /**
     * Static lock object.
     */
    private static final Object LOCK = new Object();

    /**
     * Singleton OkHttpClient.
     */
    private static final AtomicReference<OkHttpClient> OK_HTTP_CLIENT = new AtomicReference<>();

    /**
     * Serializer used for request and response objects in CapaClient.
     */
    private CapaObjectSerializer objectSerializer;

    /**
     * Creates a constructor for CapaHttp.
     * <p>
     * {@link DefaultObjectSerializer} is used for object and state serializers by default but is not recommended
     * for production scenarios.
     */
    public CapaHttpBuilder() {
        this.objectSerializer = new DefaultObjectSerializer();
    }

    /**
     * Sets the serializer for objects to be sent and received from Capa.
     * See {@link DefaultObjectSerializer} as possible serializer for non-production scenarios.
     *
     * @param objectSerializer Serializer for objects to be sent and received from Capa.
     * @return This instance.
     */
    public CapaHttpBuilder withObjectSerializer(CapaObjectSerializer objectSerializer) {
        if (objectSerializer == null) {
            throw new IllegalArgumentException("Object serializer is required");
        }
        if (objectSerializer.getContentType() == null
                || objectSerializer.getContentType().isEmpty()) {
            throw new IllegalArgumentException("Content Type should not be null or empty");
        }
        this.objectSerializer = objectSerializer;
        return this;
    }

    /**
     * Build an instance of the Http client based on the provided setup.
     *
     * @return an instance of {@link CapaHttp}
     * @throws IllegalStateException if any required field is missing
     */
    public CapaHttp build() {
        return buildCapaHttp();
    }

    /**
     * Creates an instance of the {@link CapaHttp} implementor.
     *
     * @return Instance of {@link CapaHttp} implementor
     */
    private CapaHttp buildCapaHttp() {
        if (OK_HTTP_CLIENT.get() == null) {
            synchronized (LOCK) {
                if (OK_HTTP_CLIENT.get() == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    // read timeout property
                    Duration readTimeout = Duration.ofSeconds(CapaProperties.HTTP_CLIENT_READ_TIMEOUT_SECONDS.get());
                    builder.readTimeout(readTimeout);
                    OkHttpClient okHttpClient = builder.build();
                    OK_HTTP_CLIENT.set(okHttpClient);
                }
            }
        }

        // load spi capa http impl
        return CapaClassLoader.loadComponentClassObj(
                "rpc",
                CapaHttp.class,
                new Class[]{OkHttpClient.class,
                        CapaObjectSerializer.class},
                new Object[]{OK_HTTP_CLIENT.get(),
                        this.objectSerializer});
    }
}
