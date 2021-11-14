package group.rxcloud.capa.component.telemetry.trace;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributeType;

/**
 * @author: chenyijiang
 * @date: 2021/11/11 12:41
 */
public final class StatusAttributeKey implements AttributeKey<String> {

    public static StatusAttributeKey MESSAGE = new StatusAttributeKey(AttributeKey.stringKey("_capa_error_message"));

    public static StatusAttributeKey STATUS = new StatusAttributeKey(AttributeKey.stringKey("_capa_status"));

    public static StatusAttributeKey LINK = new StatusAttributeKey(AttributeKey.stringKey("_capa_link"));

    private final AttributeKey<String> key;

    private StatusAttributeKey(AttributeKey<String> key) {
        this.key = key;
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
