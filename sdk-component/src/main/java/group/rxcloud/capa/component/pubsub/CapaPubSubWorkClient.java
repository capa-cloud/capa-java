package group.rxcloud.capa.component.pubsub;

import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import reactor.core.publisher.Mono;

public abstract class CapaPubSubWorkClient implements AutoCloseable {
  protected CapaObjectSerializer objectSerializer;

  public CapaPubSubWorkClient() {
  }

  public CapaPubSubWorkClient(CapaObjectSerializer objectSerializer) {
    this.objectSerializer = objectSerializer;
  }

  public abstract void config(PubSubConfig config);

  public abstract Mono<Void> publishEvent(PublishEventRequest request);
}
