package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.serializer.DefaultObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huijing xu
 * @date 2021/10/18
 */
public class CapaHttpTest {

    @Test
    public void testInvokeApi_Success() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TestCapaHttp capaHttp = new TestCapaHttp(builder.build(), new DefaultObjectSerializer());
        Mono<HttpResponse<String>> responseMono = capaHttp.invokeApi("post",
                null,
                null,
                null,
                headers,
                null,
                TypeRef.STRING);

        HttpResponse<String> block = responseMono.block();
        int statusCode = block.getStatusCode();

        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void testClose_Success() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TestCapaHttp capaHttp = new TestCapaHttp(builder.build(), new DefaultObjectSerializer());

        capaHttp.close();
    }
}
