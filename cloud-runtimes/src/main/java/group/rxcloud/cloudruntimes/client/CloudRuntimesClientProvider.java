package group.rxcloud.cloudruntimes.client;

/**
 * Cloud Runtimes Client Provider.
 */
public interface CloudRuntimesClientProvider {

    /**
     * Provide cloud runtimes client.
     *
     * @return the cloud runtimes client
     */
    CloudRuntimesClient provide();
}
