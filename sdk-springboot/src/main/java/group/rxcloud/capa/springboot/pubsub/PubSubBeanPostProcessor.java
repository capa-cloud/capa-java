/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package group.rxcloud.capa.springboot.pubsub;

import com.kevinten.vrml.core.beans.SpringContextConfigurator;
import group.rxcloud.capa.pubsub.Topic;
import group.rxcloud.capa.pubsub.domain.TopicSubscription;
import group.rxcloud.capa.pubsub.domain.TopicEventRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Handles Dapr annotations in Springboot Controllers.
 */
@Component
public class PubSubBeanPostProcessor implements BeanPostProcessor {

    private final EmbeddedValueResolver embeddedValueResolver;

    PubSubBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
        embeddedValueResolver = new EmbeddedValueResolver(beanFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean == null) {
            return null;
        }

        subscribeToTopics(bean.getClass(), embeddedValueResolver);

        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Subscribe to topics based on {@link Topic} annotations on the given class and any of ancestor classes.
     *
     * @param clazz Controller class where {@link Topic} is expected.
     */
    private static void subscribeToTopics(Class clazz, EmbeddedValueResolver embeddedValueResolver) {
        if (clazz == null) {
            return;
        }

        subscribeToTopics(clazz.getSuperclass(), embeddedValueResolver);

        for (Method method : clazz.getDeclaredMethods()) {
            Topic topic = method.getAnnotation(Topic.class);
            if (topic == null) {
                continue;
            }

            // get TopicSubscriber instance from spring-context
            final TopicSubscriber topicSubscriber = SpringContextConfigurator.getBean(TopicSubscriber.class);

            final String route = topic.name();

            final String topicName = embeddedValueResolver.resolveStringValue(topic.name());
            final String pubSubName = embeddedValueResolver.resolveStringValue(topic.pubsubName());
            if ((topicName != null) && (topicName.length() > 0) && pubSubName != null && pubSubName.length() > 0) {
                TopicSubscription topicSubscription = new TopicSubscription();
                topicSubscription.setPubSubName(pubSubName);
                topicSubscription.setTopicName(topicName);

                Flux<TopicEventRequest> listener = topicSubscriber.doSubscribe()
                        .apply(topicSubscription);

                listener.subscribe(topicEventRequest -> {
                    try {
                        method.invoke(topicEventRequest);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
