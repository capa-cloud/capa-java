package group.rxcloud.capa.springboot.rpc.server;

/**
 * Set custom parameters. If not necessary, use the default Settings. Note that this is set in the static phase.
 */
public interface CapaServerSettings {

    /**
     * Internal module running parameters, generally used for debugging
     */
    abstract class Internal {

    }

    /**
     * Exposes run parameters, typically for custom Settings
     */
    abstract class Public {

        /**
         * The prefix used when the service is exposed. The default is /API/
         */
        public static String SERVLET_MAPPING_PREFIX = "/api/";

        public static void setCustomServletMappingPrefix(String servletMappingPrefix) {
            SERVLET_MAPPING_PREFIX = servletMappingPrefix;
        }
    }
}
