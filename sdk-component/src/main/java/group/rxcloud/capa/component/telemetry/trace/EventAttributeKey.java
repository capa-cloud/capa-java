package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributeType;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 12:41
 */
public final class EventAttributeKey implements AttributeKey<Long> {

    public static EventAttributeKey SIZE = new EventAttributeKey(AttributeKey.longKey("_capa_event_size"));

    public static EventAttributeKey COUNT = new EventAttributeKey(AttributeKey.longKey("_capa_event_count"));

    public static EventAttributeKey FAIL = new EventAttributeKey(AttributeKey.longKey("_capa_event_fail"));

    private final AttributeKey<Long> key;

    private EventAttributeKey(AttributeKey<Long> key) {
        this.key = key;
    }

    public Integer getNum(Object value) {
        if (!(value instanceof Long)) {
            return null;
        }

        Long l = (Long) value;
        // overflow
        if (l.compareTo((long)Integer.MAX_VALUE) > 0 || l.compareTo((long)Integer.MIN_VALUE) < 0 ) {
            return null;
        }

        return ((Long) value).intValue();
    }

    @Override
    public String getKey() {
        return key.getKey();
    }

    @Override
    public AttributeType getType() {
        return key.getType();
    }
}
