package group.rxcloud.capa.nativesql;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * The Capa native sql client.
 */
public interface CapaNativeSqlClient extends CapaClient {

    @Override
    Mono<byte[]> invokeSql(String storeName, String sql, Map<String, String> metadata);
}
