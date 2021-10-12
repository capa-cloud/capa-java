package group.rxcloud.capa.springboot.pubsub;

import group.rxcloud.capa.pubsub.domain.TopicSubscription;
import group.rxcloud.capa.pubsub.domain.TopicEventRequest;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public interface TopicSubscriber {

    Function<TopicSubscription, Flux<TopicEventRequest>> doSubscribe();
}
