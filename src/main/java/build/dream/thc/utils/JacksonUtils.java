package build.dream.thc.utils;

import build.dream.thc.constants.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JacksonUtils {
    private static ConcurrentHashMap<String, ObjectMapper> objectMapperMap = new ConcurrentHashMap<String, ObjectMapper>();

    private static ObjectMapper obtainObjectMapper(String datePattern, JsonInclude.Include serializationInclusion, Module module) {
        String key = datePattern;
        if (Objects.nonNull(serializationInclusion)) {
            key += "@@@" + serializationInclusion.name();
        }

        if (Objects.nonNull(module)) {
            key += "@@@" + Objects.hashCode(module);
        }

        ObjectMapper objectMapper = objectMapperMap.get(key);
        if (Objects.nonNull(objectMapper)) {
            return objectMapper;
        }

        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(datePattern));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        if (Objects.nonNull(serializationInclusion)) {
            objectMapper.setSerializationInclusion(serializationInclusion);
        }

        if (Objects.nonNull(module)) {
            objectMapper.registerModule(module);
        }

        objectMapperMap.put(key, objectMapper);
        return objectMapper;
    }

    public static String writeValueAsString(Object object) {
        return writeValueAsString(object, Constants.DEFAULT_DATE_PATTERN, null, null);
    }

    public static String writeValueAsString(Object object, Module module) {
        return writeValueAsString(object, Constants.DEFAULT_DATE_PATTERN, null, module);
    }

    public static String writeValueAsString(Object object, JsonInclude.Include serializationInclusion) {
        return writeValueAsString(object, Constants.DEFAULT_DATE_PATTERN, serializationInclusion, null);
    }

    public static String writeValueAsString(Object object, JsonInclude.Include serializationInclusion, Module module) {
        return writeValueAsString(object, Constants.DEFAULT_DATE_PATTERN, serializationInclusion, module);
    }

    public static String writeValueAsString(Object object, String datePattern) {
        return writeValueAsString(object, datePattern, null, null);
    }

    public static String writeValueAsString(Object object, String datePattern, Module module) {
        return writeValueAsString(object, datePattern, null, module);
    }

    public static String writeValueAsString(Object object, String datePattern, JsonInclude.Include serializationInclusion, Module module) {
        return ApplicationHandler.callMethodSuppressThrow(() -> obtainObjectMapper(datePattern, serializationInclusion, module).writeValueAsString(object));
    }

    public static <T> T readValue(String content, Class<T> clazz) {
        return readValue(content, clazz, Constants.DEFAULT_DATE_PATTERN);
    }

    public static <T> T readValue(String content, Class<T> clazz, String datePattern) {
        return ApplicationHandler.callMethodSuppressThrow(() -> obtainObjectMapper(datePattern, null, null).readValue(content, clazz));
    }

    public static <T> List<T> readValueAsList(String content, Class<T> elementClass) {
        return readValueAsList(content, elementClass, Constants.DEFAULT_DATE_PATTERN);
    }

    public static <T> List<T> readValueAsList(String content, Class<T> elementClass, String datePattern) {
        return ApplicationHandler.callMethodSuppressThrow(() -> {
            ObjectMapper objectMapper = obtainObjectMapper(datePattern, null, null);
            return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass));
        });
    }

    public static <T> Set<T> readValueAsSet(String content, Class<T> elementClass) {
        return readValueAsSet(content, elementClass, Constants.DEFAULT_DATE_PATTERN);
    }

    public static <T> Set<T> readValueAsSet(String content, Class<T> elementClass, String datePattern) {
        return ApplicationHandler.callMethodSuppressThrow(() -> {
            ObjectMapper objectMapper = obtainObjectMapper(datePattern, null, null);
            return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(Set.class, elementClass));
        });
    }

    public static <K, V> Map<K, V> readValueAsMap(String content, Class<K> keyClass, Class<V> valueClass) {
        return readValueAsMap(content, keyClass, valueClass, Constants.DEFAULT_DATE_PATTERN);
    }

    public static <K, V> Map<K, V> readValueAsMap(String content, Class<K> keyClass, Class<V> valueClass, String datePattern) {
        return ApplicationHandler.callMethodSuppressThrow(() -> {
            ObjectMapper objectMapper = obtainObjectMapper(datePattern, null, null);
            return objectMapper.readValue(content, objectMapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
        });
    }
}
