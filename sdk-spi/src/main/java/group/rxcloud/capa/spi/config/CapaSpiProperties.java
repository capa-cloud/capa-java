package group.rxcloud.capa.spi.config;

import group.rxcloud.capa.infrastructure.config.CapaProperties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * The Capa SPI environment.
 */
public abstract class CapaSpiProperties {

    /**
     * Gets default options loader.
     *
     * @return the default options loader
     */
    public static CapaSpiOptionsLoader getSpiOptionsLoader() {
        // load spi capa http impl
        try {
            Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.get();
            String classPath = properties.getProperty(CapaSpiOptionsLoader.class.getName());
            Class<? extends CapaSpiOptionsLoader> aClass = (Class<? extends CapaSpiOptionsLoader>) Class.forName(classPath);
            Constructor<? extends CapaSpiOptionsLoader> constructor = aClass.getConstructor();
            Object newInstance = constructor.newInstance();
            return (CapaSpiOptionsLoader) newInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("No CapaSpiOptionsLoader implement supported.");
        }
    }
}
