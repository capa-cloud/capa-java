package group.rxcloud.capa.component.http;

import group.rxcloud.capa.infrastructure.serializer.CapaObjectSerializer;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import okhttp3.OkHttpClient;
import reactor.util.context.Context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The test capa http invoker.
 */
public class TestCapaHttp extends CapaHttp {

    public TestCapaHttp(OkHttpClient httpClient, CapaObjectSerializer objectSerializer) {
        super(httpClient, objectSerializer);
    }

    @Override
    protected <T> CompletableFuture<HttpResponse<T>> doInvokeApi(String httpMethod,
                                                                 String[] pathSegments,
                                                                 Map<String, List<String>> urlParameters,
                                                                 Object requestData,
                                                                 Map<String, String> headers,
                                                                 Context context,
                                                                 TypeRef<T> type) {
        return CompletableFuture.supplyAsync(
                () -> {
                    return new HttpResponse<>(null, null, 200);
                },
                Runnable::run);
    }
}
