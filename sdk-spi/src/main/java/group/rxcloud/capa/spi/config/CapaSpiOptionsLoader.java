package group.rxcloud.capa.spi.config;


/**
 * Read SPI configuration and generate corresponding configuration objects.
 */
public interface CapaSpiOptionsLoader<T extends RpcServiceOptions> {

    /**
     * Load rpc service options.
     *
     * @param appId the appId
     * @return the rpc service options
     */
    T loadRpcServiceOptions(String appId);
}
