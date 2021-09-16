package group.rxcloud.capa.spi.demo.config;

import group.rxcloud.capa.spi.config.RpcServiceOptions;

/**
 * RPC service options. Define for AppId.
 */
public class DemoRpcServiceOptions implements RpcServiceOptions {

    /**
     * Unique rpc service ID
     */
    private final String appId;

    /**
     * Instantiates a new Capa rpc service options.
     *
     * @param appId the app id
     */
    public DemoRpcServiceOptions(String appId) {
        this.appId = appId;
    }
}