package group.rxcloud.capa.springboot.rpc.client;


import java.util.Map;

public class ServiceClientConfig {

    private Map<String, String> headers;

    private int requestTimeoutInMilliseconds;

    public Map<String, String> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getRequestTimeoutInMilliseconds() {
        return requestTimeoutInMilliseconds;
    }

    void setRequestTimeoutInMilliseconds(int requestTimeoutInMilliseconds) {
        this.requestTimeoutInMilliseconds = requestTimeoutInMilliseconds;
    }
}
