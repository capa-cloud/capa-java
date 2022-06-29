package group.rxcloud.capa.springboot.schedule;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * The Schedule autoconfiguration.
 */
@Configuration
@AutoConfigureOrder
@Import(ScheduleAutoConfiguration.Register.class)
public class ScheduleAutoConfiguration {

    /**
     * The Register.
     */
    public static class Register implements ImportBeanDefinitionRegistrar {

        /**
         * The Schedule name.
         */
        static final String SCHEDULE_ANNOTATION = "SCHEDULE_ANNOTATION";

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(ScheduleRegister.class);
            beanDefinition.setRole(2);
            beanDefinition.setSynthetic(true);
            beanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
            beanDefinition.setAutowireCandidate(false);
            beanDefinition.setLazyInit(true);
            registry.registerBeanDefinition(SCHEDULE_ANNOTATION, beanDefinition);
        }
    }
}
