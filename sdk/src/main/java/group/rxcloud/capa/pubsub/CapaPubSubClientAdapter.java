package group.rxcloud.capa.pubsub;

import group.rxcloud.capa.component.pubsub.CapaPubSubWorkClient;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import java.util.Map;
import reactor.core.publisher.Mono;

public class CapaPubSubClientAdapter implements CapaPubSubClient{

  private final CapaPubSubWorkClient capaPubSubWorkClient;

  public CapaPubSubClientAdapter(CapaPubSubWorkClient capaPubSubWorkClient) {
    this.capaPubSubWorkClient = capaPubSubWorkClient;
  }

  @Override
  public Mono<Void> publishEvent(String pubsubName, String topicName, Object data) {
    PublishEventRequest request = new PublishEventRequest(pubsubName, topicName, data);
    return this.publishEvent(request);
  }

  @Override
  public Mono<Void> publishEvent(String pubsubName, String topicName, Object data,
      Map<String, String> metadata) {
    PublishEventRequest request = new PublishEventRequest(pubsubName, topicName, data);
    request.setMetadata(metadata);
    return this.publishEvent(request);
  }

  @Override
  public Mono<Void> publishEvent(PublishEventRequest request) {
    return capaPubSubWorkClient.publishEvent(request);
  }

  @Override
  public void close() {
    try {
      capaPubSubWorkClient.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
