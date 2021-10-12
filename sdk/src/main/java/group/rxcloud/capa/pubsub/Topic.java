/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package group.rxcloud.capa.pubsub;

import java.lang.annotation.*;

/**
 * @author dapr.io
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Topic {

    /**
     * Name of topic to be subscribed to.
     *
     * @return Topic's name.
     */
    String name();

    /**
     * Name of the pubsub bus to be subscribed to.
     *
     * @return pubsub bus's name.
     */
    String pubsubName();

    /**
     * Metadata in the form of a json object.
     * {
     * "mykey": "myvalue"
     * }
     *
     * @return metadata object
     */
    String metadata() default "{}";
}
