package group.rxcloud.capa.springboot.rpc.server;

import group.rxcloud.vrml.core.beans.SpringContextConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class CapaServerSpringInitializer implements ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CapaServerSpringInitializer.class);

    public CapaServerSpringInitializer() {
        logger.info("[Capa.Rpc.Server] server init...");
    }

    /**
     * Use `vrml` to set all spring beans to static field
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextConfigurator.setStaticApplicationContext(applicationContext);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        SpringContextConfigurator.setStaticApplicationContext(event.getApplicationContext());
    }
}
