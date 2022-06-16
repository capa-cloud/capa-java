package group.rxcloud.capa.secret;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.domain.core.secrets.GetBulkSecretRequest;
import group.rxcloud.cloudruntimes.domain.core.secrets.GetSecretRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * The Capa secret client.
 */
public interface CapaSecretClient extends CapaClient {

    @Override
    Mono<Map<String, String>> getSecret(String storeName, String secretName, Map<String, String> metadata);

    @Override
    Mono<Map<String, String>> getSecret(String storeName, String secretName);

    @Override
    Mono<Map<String, String>> getSecret(GetSecretRequest request);

    @Override
    Mono<Map<String, Map<String, String>>> getBulkSecret(String storeName);

    @Override
    Mono<Map<String, Map<String, String>>> getBulkSecret(String storeName, Map<String, String> metadata);

    @Override
    Mono<Map<String, Map<String, String>>> getBulkSecret(GetBulkSecretRequest request);
}
