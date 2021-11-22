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
package group.rxcloud.capa.pubsub.domain;

import java.util.Map;

public class TopicEventRequest {

    // id identifies the event. Producers MUST ensure that source + id
    // is unique for each distinct event. If a duplicate event is re-sent
    // (e.g. due to a network error) it MAY have the same id.
    private String id;

    // source identifies the context in which an event happened.
    // Often this will include information such as the type of the
    // event source, the organization publishing the event or the process
    // that produced the event. The exact syntax and semantics behind
    // the data encoded in the URI is defined by the event producer.
    private String source;

    // The type of event related to the originating occurrence.
    private String type;

    // The version of the CloudEvents specification.
    private String specVersion;

    // The content type of data value.
    private String contentType;

    // The content of the event.
    private Object data;

    // The pubsub topic which publisher sent to.
    private String topic;

    // The name of the pubsub the publisher sent to.
    private String pubsubName;

    // add a map to pass some extra properties.
    private Map<String, String> metadata;

    /**
     * Getter method for property id.
     *
     * @return property value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for property id.
     *
     * @param id value to be assigned to property id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for property source.
     *
     * @return property value of source
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter method for property source.
     *
     * @param source value to be assigned to property source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter method for property type.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property type.
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method for property specVersion.
     *
     * @return property value of specVersion
     */
    public String getSpecVersion() {
        return specVersion;
    }

    /**
     * Setter method for property specVersion.
     *
     * @param specVersion value to be assigned to property specVersion
     */
    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    /**
     * Getter method for property contentType.
     *
     * @return property value of contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Setter method for property contentType.
     *
     * @param contentType value to be assigned to property contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Getter method for property data.
     *
     * @return property value of data
     */
    public Object getData() {
        return data;
    }

    /**
     * Setter method for property data.
     *
     * @param data value to be assigned to property data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Getter method for property topic.
     *
     * @return property value of topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Setter method for property topic.
     *
     * @param topic value to be assigned to property topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Getter method for property pubsubName.
     *
     * @return property value of pubsubName
     */
    public String getPubsubName() {
        return pubsubName;
    }

    /**
     * Setter method for property pubsubName.
     *
     * @param pubsubName value to be assigned to property pubsubName
     */
    public void setPubsubName(String pubsubName) {
        this.pubsubName = pubsubName;
    }

    /**
     * Getter method for property metadata.
     *
     * @return property value of metadata
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Setter method for property metadata.
     *
     * @param metadata value to be assigned to property metadata
     */
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
