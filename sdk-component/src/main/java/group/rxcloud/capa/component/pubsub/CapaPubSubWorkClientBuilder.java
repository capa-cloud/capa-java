package group.rxcloud.capa.component.pubsub;


import group.rxcloud.capa.infrastructure.config.CapaProperties;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.ObjectSerializer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class CapaPubSubWorkClientBuilder {
  private ObjectSerializer objectSerializer;

  public CapaPubSubWorkClientBuilder() {
    this.objectSerializer = new DefaultObjectSerializer();
  }


  public CapaPubSubWorkClient build(PubSubConfig config) {
    CapaPubSubWorkClient capaPubSubWorkClient = buildCapaConfigStore();
    capaPubSubWorkClient.config(config);
    return capaPubSubWorkClient;
  }

  private CapaPubSubWorkClient buildCapaConfigStore() {
    try {
      Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.get();
      String capaConfigStoreClassPath = properties.getProperty(CapaPubSubWorkClient.class.getName());
      Class<? extends CapaPubSubWorkClient> aClass = (Class<? extends CapaPubSubWorkClient>) Class.forName(capaConfigStoreClassPath);
      Constructor<? extends CapaPubSubWorkClient> constructor = aClass.getConstructor(
          CapaObjectSerializer.class);
      Object newInstance = constructor.newInstance(this.objectSerializer);
      return (CapaPubSubWorkClient) newInstance;
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException("No CapaPubSub Client supported.");
    }
  }
}
