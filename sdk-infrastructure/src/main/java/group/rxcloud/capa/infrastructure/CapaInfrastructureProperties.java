package group.rxcloud.capa.infrastructure;

import group.rxcloud.capa.infrastructure.loader.CapaClassLoader;
import group.rxcloud.capa.infrastructure.metainfo.CapaEnvironment;

/**
 * Capa infrastructure common properties.
 */
public interface CapaInfrastructureProperties {

    /**
     * The Properties constants.
     */
    interface Constants {

        /**
         * The {@code component} properties prefix.
         */
        String CAPA_COMPONENT_PROPERTIES_PREFIX = "/capa-component-";
        /**
         * The {@code infrastructure} properties prefix.
         */
        String CAPA_INFRASTRUCTURE_PROPERTIES_PREFIX = "/capa-infrastructure-";

        /**
         * The properties suffix.
         */
        String CAPA_PROPERTIES_SUFFIX = ".properties";
        /**
         * The json suffix.
         */
        String CAPA_JSON_SUFFIX = ".json";
    }

    /**
     * The Settings.
     */
    abstract class Settings {

        private static final CapaEnvironment capaEnvironment;

        static {
            capaEnvironment = CapaClassLoader.loadInfrastructureClassObj("cloud", CapaEnvironment.class);
        }

        /**
         * Gets capa environment.
         *
         * @return the capa environment
         */
        public static CapaEnvironment getCapaEnvironment() {
            return capaEnvironment;
        }
    }
}
