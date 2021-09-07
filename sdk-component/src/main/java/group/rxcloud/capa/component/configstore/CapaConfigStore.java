package group.rxcloud.capa.component.configstore;


import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;

public abstract class CapaConfigStore implements AutoCloseable {

    /**
     * Capa API used in this client.
     */
    public static final String API_VERSION = "v1.0";

    /**
     * A utility class for serialize and deserialize the transient objects.
     */
    protected final CapaObjectSerializer objectSerializer;

    /**
     * Instantiates a new Capa configuration.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public CapaConfigStore(CapaObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }
}
