package group.rxcloud.capa.file;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.enhanced.file.DelFileRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.file.GetFileRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.file.GetFileResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.file.ListFileRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.file.ListFileResp;
import group.rxcloud.cloudruntimes.domain.enhanced.file.PutFileRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The Capa file client.
 */
public interface CapaFileClient extends CapaClient {

    @Override
    Mono<GetFileResponse> getFile(GetFileRequest request);

    @Override
    Mono<String> putFile(Flux<PutFileRequest> requests);

    @Override
    Mono<ListFileResp> listFile(ListFileRequest request);

    @Override
    Mono<Void> delFile(DelFileRequest request);
}
