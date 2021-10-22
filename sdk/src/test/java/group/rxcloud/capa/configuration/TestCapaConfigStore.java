package group.rxcloud.capa.configuration;

import group.rxcloud.capa.component.configstore.*;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Reckless Xu
 * @date 2021/10/19
 */
public class TestCapaConfigStore extends CapaConfigStore {

    /**
     * Instantiates a new Capa ConfigStore.
     *
     * @param objectSerializer Serializer for transient request/response objects.
     */
    public TestCapaConfigStore(CapaObjectSerializer objectSerializer) {
        super(objectSerializer);
    }

    @Override
    protected void doInit(StoreConfig storeConfig) {
        //no extra work for init
    }

    @Override
    public <T> Mono<List<ConfigurationItem<T>>> get(GetRequest getRequest, TypeRef<T> type) {
        List<ConfigurationItem<T>> result = new ArrayList<>();
        getRequest.getKeys().forEach(key -> {
            ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
            configurationItem.setKey(key);
            configurationItem.setContent(null);
            configurationItem.setGroup(getDefaultGroup());
            configurationItem.setLabel(getDefaultLabel());
            configurationItem.setTags(getRequest.getMetadata());
            configurationItem.setMetadata(getRequest.getMetadata());
            result.add(configurationItem);
        });
        return Mono.just(result);
    }

    @Override
    public <T> Flux<SubscribeResp<T>> subscribe(SubscribeReq subscribeReq, TypeRef<T> type) {
        return Flux.interval(Duration.ofSeconds(3)).map(i -> {
            SubscribeResp<T> subscribeResp = new SubscribeResp<>();
            subscribeResp.setAppId(subscribeReq.getAppId());
            subscribeResp.setStoreName(getStoreName());
            List<ConfigurationItem<T>> list = new ArrayList<>();
            subscribeReq.getKeys().forEach(key -> {
                ConfigurationItem<T> configurationItem = new ConfigurationItem<>();
                configurationItem.setKey(key);
                configurationItem.setContent(null);
                configurationItem.setGroup(getDefaultGroup());
                configurationItem.setLabel(getDefaultLabel());
                configurationItem.setTags(subscribeReq.getMetadata());
                configurationItem.setMetadata(subscribeReq.getMetadata());
                list.add(configurationItem);
            });
            subscribeResp.setItems(list);
            return subscribeResp;
        });
    }

    @Override
    public String stopSubscribe() {
        return null;
    }

    @Override
    public String getDefaultGroup() {
        return "testGroup";
    }

    @Override
    public String getDefaultLabel() {
        return "testLabel";
    }

    @Override
    public void close() throws Exception {
        //no need to close
    }
}
