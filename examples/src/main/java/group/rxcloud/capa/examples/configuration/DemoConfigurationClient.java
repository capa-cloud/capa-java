package group.rxcloud.capa.examples.configuration;

import group.rxcloud.capa.component.configstore.StoreConfig;
import group.rxcloud.capa.configuration.CapaConfigurationClient;
import group.rxcloud.capa.configuration.CapaConfigurationClientBuilder;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.ConfigurationRequestItem;
import group.rxcloud.cloudruntimes.domain.core.configuration.SubConfigurationResp;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class DemoConfigurationClient {

    public static void main(String[] args) throws InterruptedException {
        StoreConfig storeConfig = new StoreConfig();
        storeConfig.setStoreName("config");

        CapaConfigurationClient capaConfigurationClient = new CapaConfigurationClientBuilder(storeConfig).build();

        ConfigurationRequestItem configurationRequestItem = new ConfigurationRequestItem();
        configurationRequestItem.setAppId("test");
        configurationRequestItem.setStoreName("config");
        configurationRequestItem.setKeys(Collections.singletonList("test.json"));

        // get
        Mono<List<ConfigurationItem<String>>> configuration =
                capaConfigurationClient.getConfiguration(configurationRequestItem, TypeRef.STRING);

        List<ConfigurationItem<String>> block = configuration.block();

        for (ConfigurationItem<String> item : block) {
            System.out.println(item);
        }

        // subscribe
        Flux<SubConfigurationResp<String>> subConfigurationRespFlux =
                capaConfigurationClient.subscribeConfiguration(configurationRequestItem, TypeRef.STRING);

        subConfigurationRespFlux.subscribe(resp -> {
            System.out.println(resp);
        });

        Thread.sleep(1000 * 10);
    }
}
