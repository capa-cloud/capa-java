package group.rxcloud.capa.pubsub;

import group.rxcloud.capa.component.pubsub.CapaPubSubWorkClientBuilder;
import group.rxcloud.capa.component.pubsub.PubSubConfig;

public class CapaPubSubClientBuilder {

  public CapaPubSubClientBuilder() {
  }


  public CapaPubSubClient build(PubSubConfig config) {
    CapaPubSubWorkClientBuilder builder = new CapaPubSubWorkClientBuilder();
    CapaPubSubClientAdapter clientAdapter = new CapaPubSubClientAdapter(builder.build(config));
    return clientAdapter;
  }
}
