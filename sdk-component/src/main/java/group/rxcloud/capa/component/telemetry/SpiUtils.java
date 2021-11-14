package group.rxcloud.capa.component.telemetry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.rxcloud.capa.infrastructure.config.CapaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: chenyijiang
 * @date: 2021/11/14 15:23
 */
public final class SpiUtils {

    /**
     * Shared Json serializer/deserializer as per Jackson's documentation.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final Map<String, Constructor> CACHE = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(SpiUtils.class);

    private SpiUtils() {
    }

    @Nullable
    public static <T> T getFromSpiConfigFile(Class<T> type) {
        return getFromSpiConfigFile(type, null, null);
    }

    @Nullable
    public static <T> T getFromSpiConfigFile(String path, Class<T> type) {
        return getFromSpiConfigFile(path, type, null, null);
    }

    @Nullable
    public static <T> T getFromSpiConfigFile(String path, Class<T> type, Class[] argTypes, Object[] args) {
        if (path == null) {
            return null;
        }
        return load(path, type, null, null);
    }

    @Nullable
    public static <T> T getFromSpiConfigFile(Class<T> type, Class[] argTypes, Object[] args) {
        Properties properties = CapaProperties.COMPONENT_PROPERTIES_SUPPLIER.get();
        String path = properties.getProperty(type.getName());
        if (path != null) {
            return load(path, type, argTypes, args);
        }
        return null;
    }

    private static String keyOf(String path, Class type, Class[] argTypes) {
        StringBuilder builder = new StringBuilder(type.getName()).append('=').append(path);
        if (argTypes != null) {
            for (Class arg : argTypes) {
                builder.append('_').append(arg.getName());
            }
        }
        return builder.toString();
    }

    @Nonnull
    private static <T> T load(String path, Class<T> type, Class[] argTypes, Object[] args) {
        String key = keyOf(path, type, argTypes);
        Constructor<T> targetCons = CACHE.computeIfAbsent(key, k -> {
            try {
                Class<T> aClass = path == null ? type : (Class<T>) Class.forName(path);
                if (argTypes == null || argTypes.length == 0) {
                    return aClass.getConstructor();
                } else {
                    // find the first match constructor.
                    for (Constructor<?> constructor : aClass.getDeclaredConstructors()) {
                        if (constructor.getParameterCount() == argTypes.length) {
                            boolean match = true;
                            for (int i = 0; i < argTypes.length; i++) {
                                if (!isSameType(constructor.getParameterTypes()[i], args[i])) {
                                    match = false;
                                    break;
                                }
                            }
                            if (match) {
                                return constructor;
                            }
                        }
                    }

                    StringBuilder message = new StringBuilder("No constructor found. targetType = ")
                            .append(type.getName())
                            .append(" argTypes=");
                    for (Class argType : argTypes) {
                        message.append(argType.getName()).append(", ");
                    }
                    message.deleteCharAt(message.length() - 1);
                    throw new NoSuchMethodException(message.toString());
                }

            } catch (ClassNotFoundException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Fail to find the constructor. path = " + path, e);
            }
        });

        try {
            return targetCons.getParameterCount() == 0 ? targetCons.newInstance() : targetCons.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Fail to create component. targetType = " + type.getName(), e);
        }
    }

    public static <T> T loadConfigNullable(String path, Class<T> configType) {
        try (InputStream in = configType.getResourceAsStream(path)) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            return OBJECT_MAPPER.readValue(inputStreamReader, configType);
        } catch (IOException e) {
            log.warn(path + " config file not found.", e);
        }
        return null;
    }

    private static boolean isSameType(Class type, Object arg) {
        if (arg == null) {
            return !type.isPrimitive();
        }

        if (type.isInstance(arg)) {
            return true;
        }

        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return arg instanceof Boolean;
            }
            if (type == short.class) {
                return arg instanceof Short;
            }
            if (type == int.class) {
                return arg instanceof Integer;
            }
            if (type == long.class) {
                return arg instanceof Long;
            }
            if (type == float.class) {
                return arg instanceof Float;
            }
            if (type == double.class) {
                return arg instanceof Double;
            }
            if (type == byte.class) {
                return arg instanceof Byte;
            }
            if (type == char.class) {
                return arg instanceof Character;
            }
        }

        return false;
    }

}
