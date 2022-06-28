package group.rxcloud.capa.springboot.schedule;

import group.rxcloud.capa.environment.CapaEnvironmentClient;
import group.rxcloud.capa.infrastructure.exceptions.CapaExceptions;
import group.rxcloud.capa.schedule.CapaScheduleClient;
import group.rxcloud.vrml.core.serialization.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import reactor.core.publisher.Flux;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ScheduleRegister implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleRegister.class);

    @Autowired
    private CapaEnvironmentClient capaEnvironmentClient;
    @Autowired
    private CapaScheduleClient capaScheduleClient;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            Schedule scheduleAnnotation = AnnotationUtils.findAnnotation(method, Schedule.class);
            if (scheduleAnnotation != null) {
                registerSchedule(bean, method, scheduleAnnotation);
            }
        }
        return bean;
    }

    private void registerSchedule(Object bean, Method method, Schedule annotation) {
        final String appId = capaEnvironmentClient.appId();
        final String jobName = annotation.value();
        logger.info("[Capa.@Schedule.register] register schedule appId[{}] jobName[{}]",
                appId, jobName);

        Flux<Object> flux = capaScheduleClient.invokeSchedule(appId, jobName, new HashMap<>(2));
        flux.subscribe(param -> {
            logger.info("[Capa.@Schedule.invoke] schedule appId[{}] jobName[{}] invoked with param[{}]",
                    appId, jobName, Serialization.toJsonSafe(param));

            Throwable ex = null;
            try {
                method.invoke(bean, param);
            } catch (InvocationTargetException e) {
                ex = e.getTargetException();
            } catch (Throwable e) {
                ex = e;
            }
            if (ex != null) {
                logger.error("[Capa.@Schedule.invoke] schedule error", ex);
                throw CapaExceptions.propagate(ex);
            }
        });
    }
}
