package group.rxcloud.capa.configuration;


import group.rxcloud.capa.component.configstore.CapaConfigStore;
import group.rxcloud.capa.component.configstore.CapaConfigStoreBuilder;
import group.rxcloud.capa.component.configstore.StoreConfig;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A builder for the {@link CapaConfigurationClient}.
 */
public class CapaConfigurationClientBuilder {

    /**
     * Builder for Capa's ConfigStore Client.
     */
    private final List<CapaConfigStoreBuilder> configStoreBuilders;

    /**
     * Creates a constructor for {@link CapaConfigurationClient}.
     */
    public CapaConfigurationClientBuilder(StoreConfig storeConfig) {
        this(Collections.singletonList(new CapaConfigStoreBuilder(storeConfig)));
    }

    /**
     * Creates a constructor for {@link CapaConfigurationClient} with custom {@link CapaConfigStoreBuilder}.
     */
    public CapaConfigurationClientBuilder(Supplier<CapaConfigStoreBuilder> capaConfigStoreBuilderSupplier) {
        this(Collections.singletonList(capaConfigStoreBuilderSupplier.get()));
    }

    /**
     * Creates a constructor for {@link CapaConfigurationClient}.
     */
    public CapaConfigurationClientBuilder(List<CapaConfigStoreBuilder> configStoreBuilders) {
        this.configStoreBuilders = configStoreBuilders;
    }

    /**
     * Build an instance of the Client based on the provided setup.
     *
     * @return an instance of the setup Client
     */
    public CapaConfigurationClient build() {
        return buildCapaClientStore();
    }

    /**
     * Creates and instance of {@link CapaConfigurationClient}.
     */
    private CapaConfigurationClient buildCapaClientStore() {
        List<CapaConfigStore> capaConfigStores = this.configStoreBuilders.stream()
                .map(CapaConfigStoreBuilder::build)
                .collect(Collectors.toList());
        return new CapaConfigurationClientStore(capaConfigStores);
    }
}
