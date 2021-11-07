package group.rxcloud.capa.springboot.pubsub;

import group.rxcloud.capa.pubsub.domain.TopicEventRequest;
import group.rxcloud.capa.pubsub.domain.TopicSubscription;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/**
 * Defined customer topic subscriber implement.
 */
public interface TopicSubscriber {

    /**
     * Subscribe {@link TopicSubscription} and generate the message Flux.
     *
     * @return the high level function
     */
    Function<TopicSubscription, Flux<TopicEventRequest>> doSubscribe();
}
