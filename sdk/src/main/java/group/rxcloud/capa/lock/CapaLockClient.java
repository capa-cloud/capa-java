package group.rxcloud.capa.lock;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.enhanced.lock.TryLockRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.lock.TryLockResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.lock.UnlockRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.lock.UnlockResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The Capa lock client.
 */
public interface CapaLockClient extends CapaClient {

    @Override
    Mono<TryLockResponse> tryLock(TryLockRequest request);

    @Override
    Mono<UnlockResponse> unlock(UnlockRequest request);
}
