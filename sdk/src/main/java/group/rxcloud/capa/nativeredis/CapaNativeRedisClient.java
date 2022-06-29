package group.rxcloud.capa.nativeredis;

import group.rxcloud.capa.CapaClient;
import group.rxcloud.cloudruntimes.client.DefaultCloudRuntimesClient;
import group.rxcloud.cloudruntimes.domain.nativeproto.redis.geo.GeoRadiusResponse;
import group.rxcloud.cloudruntimes.domain.nativeproto.redis.geo.GeoUnit;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Capa native redis client.
 */
public interface CapaNativeRedisClient extends CapaClient {

    @Override
    Mono<byte[]> invokeRedis(String storeName, String cmd, byte[] args, Map<String, String> metadata);

    @Override
    Boolean move(String key, int dbIndex);

    @Override
    long dbSize();

    @Override
    String flushDB();

    @Override
    String flushAll();

    @Override
    String select(int index);

    @Override
    String swapDB(int index1, int index2);

    @Override
    String ping();

    @Override
    Long geoadd(String key, double longitude, double latitude, String member);

    @Override
    Double geodist(String key, String member1, String member2, GeoUnit unit);

    @Override
    List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, String withX, String sortX, int count);

    @Override
    List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, String withX, String sortX, int count);

    @Override
    List<String> geohash(String key, String... members);

    @Override
    Boolean hset(String key, String field, String value);

    @Override
    Boolean hsetnx(String key, String field, String value);

    @Override
    String hget(String key, String field);

    @Override
    Boolean hexists(String key, String field);

    @Override
    Long hdel(String key, String... fields);

    @Override
    Long hlen(String key);

    @Override
    Long hstrlen(String key, String field);

    @Override
    Long hincrBy(String key, String field, long increment);

    @Override
    Double hincrByFloat(String key, String field, double increment);

    @Override
    Boolean hmset(String key, Map<String, String> keyValues);

    @Override
    List<String> hmget(String key, String... fields);

    @Override
    Set<String> hkeys(String key);

    @Override
    List<String> hvals(String key);

    @Override
    Map<String, String> hgetAll(String key);

    @Override
    Boolean exists(String key);

    @Override
    String type(String key);

    @Override
    String rename(String oldkey, String newkey);

    @Override
    long renamenx(String oldkey, String newkey);

    @Override
    Long del(String... keys);

    @Override
    String randomKey();

    @Override
    Set<String> keys(String pattern);

    @Override
    Boolean expire(String key, long seconds);

    @Override
    Boolean expireAt(String key, long unixtime);

    @Override
    Long ttl(String key);

    @Override
    Boolean persist(String key);

    @Override
    Boolean pexpire(String key, long milliseconds);

    @Override
    long pexpireAt(String key, long millisecondsTimestamp);

    @Override
    long pttl(String key);

    @Override
    Long lpush(String key, String... elements);

    @Override
    Long lpushx(String key, String... elements);

    @Override
    Long rpush(String key, String... elements);

    @Override
    Long rpushx(String key, String... elements);

    @Override
    String lpop(String key);

    @Override
    String rpop(String key);

    @Override
    String rpoplpush(String src, String dst);

    @Override
    Long lrem(String key, long count, String element);

    @Override
    Long llen(String key);

    @Override
    String lindex(String key, long index);

    @Override
    Long linsert(String key, String beforeORafter, String pivot, String value);

    @Override
    Boolean lset(String key, long index, String element);

    @Override
    List<String> lrange(String key, long start, long end);

    @Override
    Boolean ltrim(String key, long start, long end);

    @Override
    List<String> blpop(int timeout, String... keys);

    @Override
    List<String> brpop(int timeout, String... keys);

    @Override
    String brpoplpush(String src, String dst, int timeout);

    @Override
    Long sadd(String key, String... members);

    @Override
    Boolean sismember(String key, String member);

    @Override
    Set<String> spop(String key, long count);

    @Override
    List<String> srandmember(String key, long count);

    @Override
    Long srem(String key, String... members);

    @Override
    long smove(String srckey, String dstkey, String member);

    @Override
    Long scard(String key);

    @Override
    Set<String> smembers(String key);

    @Override
    Set<String> sinter(String... keys);

    @Override
    long sinterstore(String dstkey, String... keys);

    @Override
    Set<String> sunion(String... keys);

    @Override
    long sunionstore(String dstkey, String... keys);

    @Override
    Set<String> sdiff(String... keys);

    @Override
    long sdiffstore(String dstkey, String... keys);

    @Override
    Long zadd(String key, Map<String, Double> scoresAndMembers);

    @Override
    Double zscore(String key, String member);

    @Override
    Double zincrby(String key, double increment, String member);

    @Override
    Long zcard(String key);

    @Override
    Long zcount(String key, double min, double max);

    @Override
    Set<String> zrange(String key, long start, long end);

    @Override
    Set<String> zrevrange(String key, long start, long end);

    @Override
    Set<String> zrangeByScore(String key, double min, double max, int offset, int count);

    @Override
    Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count);

    @Override
    Long zrank(String key, String member);

    @Override
    Long zrevrank(String key, String member);

    @Override
    Long zrem(String key, String... member);

    @Override
    Long zremrangeByRank(String key, long start, long end);

    @Override
    Long zremrangeByScore(String key, double start, double end);

    @Override
    Set<String> zrangeByLex(String key, String min, String max, int offset, int count);

    @Override
    long zlexcount(String key, String min, String max);

    @Override
    Long zremrangeByLex(String key, String min, String max);

    @Override
    long zunionstore(String dstkey, String... sets);

    @Override
    long zinterstore(String dstkey, String... sets);

    @Override
    Boolean set(String key, String value, String nxxx, String expx, long time);

    @Override
    Boolean setnx(String key, String value);

    @Override
    Boolean setex(String key, long seconds, String value);

    @Override
    Boolean psetex(String key, long milliseconds, String value);

    @Override
    String get(String key);

    @Override
    String getSet(String key, String value);

    @Override
    Long strlen(String key);

    @Override
    Long append(String key, String value);

    @Override
    Long setrange(String key, long offset, String value);

    @Override
    String getrange(String key, long startOffset, long endOffset);

    @Override
    Long incr(String key);

    @Override
    Long incrBy(String key, long amount);

    @Override
    Double incrByFloat(String key, double amount);

    @Override
    Long decr(String key);

    @Override
    Long decrBy(String key, long amount);

    @Override
    String mset(String... keysvalues);

    @Override
    Boolean msetnx(String... keysvalues);

    @Override
    List<String> mget(String... keys);
}
