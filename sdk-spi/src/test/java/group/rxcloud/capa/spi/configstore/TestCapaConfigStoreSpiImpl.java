package group.rxcloud.capa.spi.configstore;

import group.rxcloud.capa.component.configstore.*;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class TestCapaConfigStoreSpiImpl extends CapaConfigStoreSpi {


    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public TestCapaConfigStoreSpiImpl(CapaObjectSerializer objectSerializer) {
        super(objectSerializer);
    }

    @Override
    protected <T> Mono<List<ConfigurationItem<T>>> doGet(String appId, String group, String label, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        List<ConfigurationItem<T>> result = new ArrayList<>();
        keys.forEach(key -> {
            ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
            configurationItem.setKey(key);
            configurationItem.setContent(null);
            configurationItem.setGroup(group);
            configurationItem.setLabel(label);
            configurationItem.setTags(metadata);
            configurationItem.setMetadata(metadata);
            result.add(configurationItem);
        });
        return Mono.just(result);
    }

    @Override
    protected <T> Flux<SubscribeResp<T>> doSubscribe(String appId, String group, String label, List<String> keys, Map<String, String> metadata, TypeRef<T> type) {
        return Flux.interval(Duration.ofSeconds(3)).map(i -> {
            SubscribeResp<T> subscribeResp = new SubscribeResp<>();
            subscribeResp.setAppId(appId);
            subscribeResp.setStoreName(getStoreName());
            List<ConfigurationItem<T>> list = new ArrayList<>();
            keys.forEach(key -> {
                ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
                configurationItem.setKey(key);
                configurationItem.setContent(null);
                configurationItem.setGroup(group);
                configurationItem.setLabel(label);
                configurationItem.setTags(metadata);
                configurationItem.setMetadata(metadata);
                list.add(configurationItem);
            });
            subscribeResp.setItems(list);
            return subscribeResp;
        });
    }

    @Override
    protected void doInit(StoreConfig storeConfig) {
        //do nothing
    }

    @Override
    public String stopSubscribe() {
        return null;
    }

    @Override
    public void close() throws Exception {
        //do nothing
    }
}
