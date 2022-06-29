package group.rxcloud.capa.database;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.enhanced.database.CreateTableRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.CreateTableResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.DeleteTableRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.DeleteTableResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.GetConnectionRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.GetConnectionResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.InsertRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.InsertResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.QueryRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.QueryResponse;
import group.rxcloud.cloudruntimes.domain.enhanced.database.UpdateRequest;
import group.rxcloud.cloudruntimes.domain.enhanced.database.UpdateResponse;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The Capa database client.
 */
public interface CapaDatabaseClient extends CapaClient {

    @Override
    Mono<GetConnectionResponse> getConnection(GetConnectionRequest req);

    @Override
    Mono<CreateTableResponse> createTable(CreateTableRequest req);

    @Override
    Mono<DeleteTableResponse> deleteTable(DeleteTableRequest req);

    @Override
    Mono<InsertResponse> insert(InsertRequest req);

    @Override
    Mono<InsertResponse> insert(String dbName, String tableName, Object data);

    @Override
    <T> Mono<QueryResponse<T>> query(QueryRequest req, TypeRef<T> type);

    @Override
    <T> Mono<QueryResponse<T>> query(String dbName, String tableName, Object data, TypeRef<T> type);

    @Override
    Mono<UpdateResponse> update(UpdateRequest req);

    @Override
    Mono<UpdateResponse> update(String dbName, String tableName, Object data);

    @Override
    Mono<Void> BeginTransaction();

    @Override
    Mono<Void> UpdateTransaction();

    @Override
    Mono<Void> QueryTransaction();

    @Override
    Mono<Void> CommitTransaction();

    @Override
    Mono<Void> RollbackTransaction();
}
