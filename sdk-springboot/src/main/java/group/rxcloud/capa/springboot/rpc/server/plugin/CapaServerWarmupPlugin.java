package group.rxcloud.capa.springboot.rpc.server.plugin;

/**
 * The Capa server warmup plugin.
 */
public interface CapaServerWarmupPlugin {

    /**
     * True will skip warmup logic.
     */
    boolean isInited();

    /**
     * Custom sync warmup logic.
     */
    void warmup();
}
