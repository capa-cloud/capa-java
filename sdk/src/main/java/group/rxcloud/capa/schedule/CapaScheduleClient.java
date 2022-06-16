package group.rxcloud.capa.schedule;

import group.rxcloud.capa.CapaClient;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * The Capa schedule client.
 */
public interface CapaScheduleClient extends CapaClient {

    @Override
    Flux<Object> invokeSchedule(String appId, String jobName, Map<String, String> metadata);
}
