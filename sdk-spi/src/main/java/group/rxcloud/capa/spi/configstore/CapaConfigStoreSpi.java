package group.rxcloud.capa.spi.configstore;

import group.rxcloud.capa.component.configstore.*;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The SPI Capa ConfigStore client. Templates for different implementations.
 */
public abstract class CapaConfigStoreSpi extends CapaConfigStore {

    private static final Logger logger = LoggerFactory.getLogger(CapaConfigStoreSpi.class);

    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public CapaConfigStoreSpi(CapaObjectSerializer objectSerializer) {
        super(objectSerializer);
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> get(GetRequest getRequest, TypeRef<T> type) {
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaConfigStoreSpi] get config request[{}]", getRequest);
        }
        final String appId = getRequest.getAppId();
        final String group = getRequest.getGroup();
        final String label = getRequest.getLabel();
        final Map<String, String> metadata = getRequest.getMetadata();

        // [config_name, value1, value2, ...]
        final List<String> keys = getRequest.getKeys();
        Objects.requireNonNull(keys, "keys");
        final String configName = keys.get(0);
        final List<String> values = keys.subList(1, keys.size());
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaConfigStoreSpi] get config[config_name={}, values={}]",
                    configName, keys);
        }

        return doGet(appId, group, label, configName, values, metadata, type);
    }

    /**
     * Get configuration.
     *
     * @param <T>        the response type parameter
     * @param appId      the app id
     * @param group      the group
     * @param label      the label
     * @param configName the config name
     * @param values     the values
     * @param metadata   the metadata
     * @param type       the response type
     * @return the async mono response
     */
    protected abstract <T> Mono<List<ConfigurationItem<T>>> doGet(String appId,
                                                                  String group,
                                                                  String label,
                                                                  String configName,
                                                                  List<String> values,
                                                                  Map<String, String> metadata,
                                                                  TypeRef<T> type);

    @Override
    public <T> Flux<SubscribeResp<T>> subscribe(SubscribeReq subscribeReq, TypeRef<T> type) {
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaConfigStoreSpi] subscribe config request[{}]", subscribeReq);
        }
        final String appId = subscribeReq.getAppId();
        final String group = subscribeReq.getGroup();
        final String label = subscribeReq.getLabel();
        final Map<String, String> metadata = subscribeReq.getMetadata();

        // [config_name, value1, value2, ...]
        final List<String> keys = subscribeReq.getKeys();
        Objects.requireNonNull(keys, "keys");
        final String configName = keys.get(0);
        final List<String> values = keys.subList(1, keys.size());
        if (logger.isDebugEnabled()) {
            logger.debug("[CapaConfigStoreSpi] subscribe config[config_name={}, values={}]",
                    configName, keys);
        }

        return doSubscribe(appId, group, label, configName, values, metadata, type);
    }

    /**
     * Subscribe configuration.
     *
     * @param <T>        the response type parameter
     * @param appId      the app id
     * @param group      the group
     * @param label      the label
     * @param configName the config name
     * @param values     the values
     * @param metadata   the metadata
     * @param type       the response type
     * @return the async flux of response stream
     */
    protected abstract <T> Flux<SubscribeResp<T>> doSubscribe(String appId,
                                                              String group,
                                                              String label,
                                                              String configName,
                                                              List<String> values,
                                                              Map<String, String> metadata,
                                                              TypeRef<T> type);

    @Override
    public String getDefaultGroup() {
        return "application";
    }

    @Override
    public String getDefaultLabel() {
        return "";
    }
}
