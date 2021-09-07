package group.rxcloud.capa.configuration;


import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SaveConfigurationRequest;
import reactor.core.publisher.Mono;

/**
 * Abstract class with convenient methods common between client implementations.
 *
 * @see CapaConfigurationClientStore
 */
public abstract class AbstractCapaConfigurationClient implements CapaConfigurationClient {

    @Override
    public Mono<Void> saveConfiguration(SaveConfigurationRequest saveConfigurationRequest) {
        return Mono.error(new UnsupportedOperationException("unsupported save configuration"));
    }

    @Override
    public Mono<Void> deleteConfiguration(ConfigurationRequestItem configurationRequestItem) {
        return Mono.error(new UnsupportedOperationException("unsupported delete configuration"));
    }
}
