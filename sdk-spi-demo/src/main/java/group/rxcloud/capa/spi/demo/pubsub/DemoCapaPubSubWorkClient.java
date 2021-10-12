package group.rxcloud.capa.spi.demo.pubsub;

import group.rxcloud.capa.component.pubsub.CapaPubSubWorkClient;
import group.rxcloud.capa.component.pubsub.PubSubConfig;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.domain.core.pubsub.PublishEventRequest;
import java.io.IOException;
import reactor.core.publisher.Mono;

public class DemoCapaPubSubWorkClient extends CapaPubSubWorkClient {

  public DemoCapaPubSubWorkClient(CapaObjectSerializer objectSerializer) {
    super(objectSerializer);
  }

  @Override
  public void config(PubSubConfig config) {
    System.out.println("config");
  }

  @Override
  public Mono<Void> publishEvent(PublishEventRequest request) {
    try {
      System.out.println(new String(objectSerializer.serialize(request)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Mono.empty();
  }

  @Override
  public void close() throws Exception {
    System.out.println("close");
  }
}
