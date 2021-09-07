package group.rxcloud.capa.configuration;

import group.rxcloud.capa.component.configstore.CapaConfigStore;
import group.rxcloud.capa.infrastructure.exceptions.CapaExceptions;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * An adapter for the Config Store Client.
 */
public class CapaConfigurationClientStore extends AbstractCapaConfigurationClient {

    /**
     * The Store client to be used.
     *
     * @see CapaConfigStore
     */
    private final CapaConfigStore store;

    public CapaConfigurationClientStore(CapaConfigStore store) {
        this.store = store;
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> getConfiguration(ConfigurationRequestItem configurationRequestItem, TypeRef<T> type) {
        try {
            String storeName = configurationRequestItem.getStoreName();
            String appId = configurationRequestItem.getAppId();
            String group = configurationRequestItem.getGroup();
            String label = configurationRequestItem.getLabel();
            List<String> keys = configurationRequestItem.getKeys();
            Map<String, String> metadata = configurationRequestItem.getMetadata();
            // check storeName
            if (storeName == null || storeName.trim().isEmpty()) {
                throw new IllegalArgumentException("Store Name cannot be null or empty.");
            }
            // check appId
            if (appId == null || appId.trim().isEmpty()) {
                throw new IllegalArgumentException("App Id cannot be null or empty.");
            }

            // TODO: 2021/8/6

            return Mono.empty();
        } catch (Exception ex) {
            return CapaExceptions.wrapMono(ex);
        }
    }

    @Override
    public <T> Flux<SubConfigurationResp<T>> subscribeConfiguration(ConfigurationRequestItem configurationRequestItem, TypeRef<T> type) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            store.close();
        } catch (Exception e) {
            throw CapaExceptions.propagate(e);
        }
    }
}
