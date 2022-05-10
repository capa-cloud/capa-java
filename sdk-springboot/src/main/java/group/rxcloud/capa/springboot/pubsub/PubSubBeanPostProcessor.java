/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.springboot.pubsub;

import group.rxcloud.vrml.core.beans.SpringContextConfigurator;
import group.rxcloud.vrml.core.serialization.Serialization;
import group.rxcloud.capa.infrastructure.exceptions.CapaException;
import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * Handles Capa annotations in Springboot Controllers.
 */
@Component
public class PubSubBeanPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PubSubBeanPostProcessor.class);

    private final EmbeddedValueResolver embeddedValueResolver;
    private final CapaObjectSerializer serializer;

    PubSubBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
        embeddedValueResolver = new EmbeddedValueResolver(beanFactory);
        serializer = new DefaultObjectSerializer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean == null) {
            return null;
        }

        subscribeToTopics(bean.getClass(), embeddedValueResolver, serializer);

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
     * @param clazz      Controller class where {@link Topic} is expected.
     * @param serializer json serializer
     */
    private static void subscribeToTopics(Class clazz, EmbeddedValueResolver embeddedValueResolver, CapaObjectSerializer serializer) {
        if (clazz == null) {
            return;
        }

        subscribeToTopics(clazz.getSuperclass(), embeddedValueResolver, serializer);

        // get TopicSubscriber instance from spring-context
        final TopicSubscriber topicSubscriber = SpringContextConfigurator.getBean(TopicSubscriber.class);

        for (Method method : clazz.getDeclaredMethods()) {
            Topic topic = method.getAnnotation(Topic.class);
            if (topic == null) {
                continue;
            }

            final String topicName = embeddedValueResolver.resolveStringValue(topic.name());
            final String pubSubName = embeddedValueResolver.resolveStringValue(topic.pubsubName());
            boolean isValidTopic = (topicName != null) && (topicName.length() > 0) && pubSubName != null && pubSubName.length() > 0;
            if (!isValidTopic) {
                if (logger.isWarnEnabled()) {
                    logger.warn("[PubSub.@Topic.subscribe] illegal pubsub[{}] topic[{}]",
                            pubSubName, topicName);
                }
                continue;
            }
            final Map<String, String> metadata = resolveMetadataMap(serializer, topic.metadata());

            TopicSubscription topicSubscription = new TopicSubscription();
            topicSubscription.setPubSubName(pubSubName);
            topicSubscription.setTopicName(topicName);
            topicSubscription.setMetadata(metadata);

            if (logger.isInfoEnabled()) {
                logger.info("[PubSub.@Topic.subscribe] try to generate pubsub[{}] topic[{}] metadata[{}] listener",
                        pubSubName, topicName, metadata);
            }

            // subscribe topic and generate the flux
            Flux<TopicEventRequest> listener = topicSubscriber.doSubscribe(topicSubscription);

            listener.subscribe(topicEventRequest -> {
                try {
                    if (logger.isDebugEnabled(
                    )) {
                        logger.debug("[PubSub.@Topic.subscribe] request[{}]", Serialization.toJsonSafe(topicEventRequest));
                    }
                    method.invoke(topicEventRequest);
                } catch (IllegalAccessException e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("[PubSub.@Topic.subscribe] IllegalAccessException, request[{}]", Serialization.toJsonSafe(topicEventRequest), e);
                    }
                    throw new CapaException(e);
                } catch (InvocationTargetException e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("[PubSub.@Topic.subscribe] InvocationTargetException, request[{}]", Serialization.toJsonSafe(topicEventRequest), e);
                    }
                    throw new CapaException(e);
                } catch (Exception e) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("[PubSub.@Topic.subscribe] Exception, request[{}]", Serialization.toJsonSafe(topicEventRequest), e);
                    }
                    throw new CapaException(e);
                }
            });
        }
    }

    private static Map<String, String> resolveMetadataMap(CapaObjectSerializer serializer, String metadata) {
        Map<String, String> metadataMap = Collections.emptyMap();
        if (!StringUtils.isEmpty(metadata)) {
            byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
            try {
                metadataMap = serializer.deserialize(metadataBytes, TypeRef.get(Map.class));
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("[PubSub.@Topic.subscribe] illegal metadata[{}]",
                            metadata, e);
                }
                throw new CapaException(e);
            }
        }
        return metadataMap;
    }
}
