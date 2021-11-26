package group.rxcloud.capa.infrastructure.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: chenyijiang
 * @date: 2021/11/26 12:55
 */
public class SpiUtilsTest {

    @Test
    public void loadConfigNullable() {
        Config config = SpiUtils.loadConfigNullable("/config.json", Config.class);
        assertEquals("aaa", config.str);
        assertEquals(2, config.getList().size());
        assertEquals("zzz", config.getList().get(0));

        assertNull(SpiUtils.loadConfigNullable("aaa", Config.class));
        assertNull(SpiUtils.loadConfigNullable("/config.json", Integer.class));
    }

    @Test
    public void loadPropertiesNullable() {
        Properties properties = SpiUtils.loadPropertiesNullable("/config.properties");
        assertEquals("aaa", properties.getProperty("str"));

        assertNull(SpiUtils.loadPropertiesNullable("lalala"));
    }

    @Test
    public void loadProperties() {
        Properties properties = SpiUtils.loadProperties("/config.properties");
        assertEquals("aaa", properties.getProperty("str"));
    }

    @Test
    public void loadPropertiesFail() {
        Throwable t = null;
        try {
            Properties properties = SpiUtils.loadProperties("aaaa");
        } catch (Throwable throwable) {
            t = throwable;
        }
        assertNotNull(t);
    }
    @Test
    public void loadFromSpiComponentFileNullable() {
        MyInterface myInterface = SpiUtils.loadFromSpiComponentFileNullable(MyInterface.class, "test");
        assertTrue(myInterface instanceof MyImpl);
    }

    @Test
    public void loadFromSpiComponentFileNullable2() {
        MyInterfaceWithArgs myInterfaceWithArgs = SpiUtils.loadFromSpiComponentFileNullable(MyInterfaceWithArgs.class, new Class[] {boolean.class, short.class, int.class, long.class, float.class, double.class, byte.class, char.class, String.class}, new Object[] {true, (short)1, 2, 3L, 4.0f, 5.0, (byte)0x1, 'x', "a"}, "test", true);

        assertTrue(myInterfaceWithArgs instanceof MyImplWithArgs);


    }

    @Test
    public void loadFromSpiComponentFileNullable2Fail() {
        MyInterfaceWithArgs myInterfaceWithArgs = SpiUtils.loadFromSpiComponentFileNullable(MyInterfaceWithArgs.class, "test");
        assertNull(myInterfaceWithArgs);


        assertNull(SpiUtils.loadFromSpiComponentFileNullable(MyImplWithArgs.class, new Class[] {String.class}, new Object[] {"a"}, "test", true));

        assertNull(SpiUtils.loadFromSpiComponentFileNullable(MyInterfaceWithArgs.class, new Class[] {Long.class}, new Object[] {1L}, "test", false));

        assertNull(SpiUtils.loadFromSpiComponentFileNullable(MyInterfaceWithArgs.class, new Class[] {Integer.class}, new Object[] {1}, "test", false));

        assertNull(SpiUtils.loadFromSpiComponentFileNullable(MyInterfaceWithArgs.class, new Class[] {boolean.class, short.class, int.class, long.class, float.class, double.class, byte.class, char.class, String.class}, new Object[] {true, (short)1, 2, 3L, 4.0f, 5.0, null, 'x', "a"}, "test", true));


    }

    @Test
    public void newInstance() {
        assertNull(SpiUtils.newInstance(null, Config.class, null, null, false));

        MyImpl my = SpiUtils.newInstanceWithConstructorCache(MyImpl.class.getCanonicalName(), MyImpl.class);
        assertNotNull(my);

        MyImplWithArgs myImplWithArgs = SpiUtils.newInstance(MyImplWithArgs.class.getCanonicalName(),MyImplWithArgs.class,  new Class[] {String.class}, new Object[] {"a"}, false);
        assertNotNull(myImplWithArgs);
    }

    @Test
    public void newInstanceFail() {
        Throwable t = null;
        try {
            Config my = SpiUtils.newInstanceWithConstructorCache(Config.class.getCanonicalName(), Config.class);
        } catch (Throwable throwable) {
            t = throwable;
        }
        assertNotNull(t);
    }

    static class Config {

        private String str;

        private List<String> list;

        public String getStr() {
            return str;
        }

        public Config setStr(String str) {
            this.str = str;
            return this;
        }

        public List<String> getList() {
            return list;
        }

        public Config setList(List<String> list) {
            this.list = list;
            return this;
        }
    }
}