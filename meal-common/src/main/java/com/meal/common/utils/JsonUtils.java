package com.meal.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

public final class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper SIMPLE_OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    private JsonUtils(){}

    public static String toJson(Object javaObject) {
        try {
            return OBJECT_MAPPER.writeValueAsString(javaObject);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Can't resolve from class: %s.", javaObject.getClass()), e);
        }
    }

    public static Map<String, Object> toMap(Object obj) {
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<>() {});
    }

    public static<T> T convert(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }

    public static byte[] toBytes(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Can't resolve from class: %s.", obj.getClass()), e);
        }
    }

    public static byte[] toBytesIfNotNull(Object obj) {
        Map<String, Object> objectMap = toMap(obj);
        objectMap.entrySet().removeIf((entry)-> Objects.isNull(entry.getValue()));
        return toBytes(objectMap);
    }

    public static <T> T to(String content, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Can't resolve json: %s to class: %s.", content, clazz), e);
        }
    }

    public static <T> T toGeneric(String content, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Can't resolve json: %s to class: %s.",
                    content, typeReference.getType().getTypeName()), e);
        }
    }
    public static <T> T fromJson(Object o, Class<T> classOfT) {
        return fromJson(toJson(o), classOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return mapper.readValue(json, classOfT);
        } catch (Exception e) {
            processException(json, e);
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            processException(json, e);
        }
        return null;
    }

    public static <T> T fromJson(String json, String className) {
        try {
            JavaType javaType = constructType(className);
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            processException(json, e);
        }
        return null;
    }
    private static void processException(Object object, Exception e) {
        throw new IllegalArgumentException("json process error.", e);
    }
    static JavaType constructType(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return mapper.constructType(clazz);
    }
    public static String toJsonKeepNullValue(Object object) {
        try {
            return SIMPLE_OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Can not parse object: %s to json string.", object.getClass()));
        }
    }
}
