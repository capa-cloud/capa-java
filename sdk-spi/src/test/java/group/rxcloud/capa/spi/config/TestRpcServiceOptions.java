package group.rxcloud.capa.spi.config;

/**
 * RPC service options used in tests only.
 */
public class TestRpcServiceOptions implements RpcServiceOptions {

    /**
     * Unique rpc service ID
     */
    private final String appId;

    /**
     * Instantiates a new Capa rpc service options.
     *
     * @param appId the app id
     */
    public TestRpcServiceOptions(String appId) {
        this.appId = appId;
    }
}
