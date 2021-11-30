package group.rxcloud.capa.infrastructure.hook;

public abstract class Mixer {

    private static ConfigurationHooks configurationHooks;

    private static TelemetryHooks telemetryHooks;

    public static void setConfigurationHooks(ConfigurationHooks configurationHooks) {
        Mixer.configurationHooks = configurationHooks;
    }

    public static void setTelemetryHooks(TelemetryHooks telemetryHooks) {
        Mixer.telemetryHooks = telemetryHooks;
    }

    public static ConfigurationHooks getConfigurationHooks() {
        return configurationHooks;
    }

    public static TelemetryHooks getTelemetryHooks() {
        return telemetryHooks;
    }
}
