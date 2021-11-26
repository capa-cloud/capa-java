package group.rxcloud.capa.component.telemetry.trace;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 23:00
 */
public class EventAttributeKeyTest {

    @Test
    public void getNum() {
        assertNull(EventAttributeKey.COUNT.getNum("aaa"));

        assertNull(EventAttributeKey.COUNT.getNum(Long.MAX_VALUE));
        assertNull(EventAttributeKey.COUNT.getNum(Long.MIN_VALUE));
        assertEquals(22, EventAttributeKey.COUNT.getNum(22L).intValue());
    }
}