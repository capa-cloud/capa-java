package group.rxcloud.capa.component.http;

import java.util.Map;

/**
 * {@link CapaHttp} HTTP Client Response Struct.
 *
 * @param <T> the Actual response data type.
 */
public class HttpResponse<T> {

    private final T body;
    private final Map<String, String> headers;
    private final int statusCode;

    /**
     * Represents an http response.
     *
     * @param body       The body of the http response.
     * @param headers    The headers of the http response.
     * @param statusCode The status code of the http response.
     */
    public HttpResponse(T body, Map<String, String> headers, int statusCode) {
        this.body = body;
        this.headers = headers;
        this.statusCode = statusCode;
    }

    /**
     * Gets actual response data.
     */
    public T getBody() {
        return body;
    }

    /**
     * Gets http headers.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets http invocation status code.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
