package group.rxcloud.capa.springboot.rpc.client;

import java.util.Map;

public class ServiceClientConfigBuilder {

    private Map<String, String> headers;

    private int requestTimeoutInMilliseconds;

    public ServiceClientConfigBuilder() {
    }

    public ServiceClientConfigBuilder withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ServiceClientConfigBuilder withRequestTimeoutInMilliseconds(int requestTimeoutInMilliseconds) {
        this.requestTimeoutInMilliseconds = requestTimeoutInMilliseconds;
        return this;
    }

    public ServiceClientConfig build() {
        ServiceClientConfig serviceClientConfig = new ServiceClientConfig();
        serviceClientConfig.setHeaders(this.headers);
        serviceClientConfig.setRequestTimeoutInMilliseconds(this.requestTimeoutInMilliseconds);
        return serviceClientConfig;
    }
}
