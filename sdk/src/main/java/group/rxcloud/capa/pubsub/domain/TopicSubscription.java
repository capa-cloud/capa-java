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

public class TopicSubscription {

    /**
     * Required. The name of the pubsub containing the topic below to subscribe to.
     */
    private String pubSubName;
    /**
     * Required. The name of topic which will be subscribed
     */
    private String topicName;
    /**
     * The optional properties used for this topic's subscription e.g. session id
     */
    private Map<String, String> metadata;

    public String getPubSubName() {
        return pubSubName;
    }

    public void setPubSubName(String pubSubName) {
        this.pubSubName = pubSubName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
