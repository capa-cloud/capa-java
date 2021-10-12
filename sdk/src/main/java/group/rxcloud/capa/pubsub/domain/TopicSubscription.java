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
