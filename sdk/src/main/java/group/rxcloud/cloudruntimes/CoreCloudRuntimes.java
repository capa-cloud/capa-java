package group.rxcloud.cloudruntimes;

import group.rxcloud.cloudruntimes.domain.core.*;

/**
 * Core Cloud Runtimes standard API defined.
 */
public interface CoreCloudRuntimes extends
        InvocationRuntimes,
        PubSubRuntimes,
        BindingRuntimes,
        StateRuntimes,
        SecretsRuntimes,
        ConfigurationRuntimes {
}
