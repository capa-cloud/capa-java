package group.rxcloud.capa.examples.pubsub;

import group.rxcloud.capa.pubsub.CapaPubSubClient;
import group.rxcloud.capa.pubsub.CapaPubSubClientBuilder;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;

public class DemoPubSubClient {

  public static void main(String[] args) {
    CapaPubSubClientBuilder builder = new CapaPubSubClientBuilder();
    CapaPubSubClient capaPubSubClient = builder.build(null);
    PublishEventRequest request = new PublishEventRequest("pub", "topic", "data");
    capaPubSubClient.publishEvent(request);
  }
}
