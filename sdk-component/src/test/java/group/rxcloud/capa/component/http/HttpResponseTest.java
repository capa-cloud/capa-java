package group.rxcloud.capa.component.http;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseTest {

    @Test
    public void testGet_Success() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse<String> httpResponse = new HttpResponse<String>("body", headers, 200);

        Assert.assertEquals("body", httpResponse.getBody());

        Map<String, String> resultMap = headers;
        if (httpResponse.getHeaders() != null) {
            resultMap = httpResponse.getHeaders();
        }
        Assert.assertEquals("application/json", resultMap.get("Content-Type"));

        Assert.assertEquals(200, httpResponse.getStatusCode());
    }


}
