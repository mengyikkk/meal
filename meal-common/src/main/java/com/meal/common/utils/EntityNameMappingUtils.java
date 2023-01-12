package com.meal.common.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EntityNameMappingUtils {
    private EntityNameMappingUtils() {}

    /**
     * 排序字段进行SQL排序标准封装.
     * <pre>
     *     + = ASC
     *     - = DESC
     * </pre>
     * 字段与列名的转换规则以{@link #findColumnNameUnderscoreToCamelCase(Class)}为实现方式
     *
     * @param clazz   字段实体
     * @param columns 排序列列名列表
     * @param <T>     class类型
     * @return 映射表中的列名列表，并且标有排序SQL关键字
     */
    public static <T extends Serializable> List<String> orderColumnNameUnderscoreToCameCase(Class<T> clazz, List<String> columns) {
        if (Objects.isNull(columns) || columns.isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> entityColumns = new HashSet<>(findColumnNameUnderscoreToCamelCase(clazz));
        return columns.stream().distinct()
                .filter(name -> entityColumns.contains(name.startsWith("+") || name.startsWith("-") ? name.substring(1) : name))
                .map(name -> String.format("%s %s", name.startsWith("+") || name.startsWith("-") ? name.substring(1) : name, name.startsWith("-") ? "DESC" : "ASC"))
                .collect(Collectors.toList());
    }

    /**
     * 获取实体映射的列名列表,遵守java bean规范以及数据库命名规范
     * 该操作会将java字段命名规范转换为数据库命名规范
     * 例: underName to under_name
     *
     * @param clazz 实体class
     * @param <T>   class类型
     * @return 映射表中的列名列表
     */
    public static <T extends Serializable> List<String> findColumnNameUnderscoreToCamelCase(Class<T> clazz) {
        return findColumnNames(clazz).stream().map(columnName -> {
            StringBuilder builder = new StringBuilder();
            char[] chars = columnName.toCharArray();
            int length = chars.length;
            for (int i = 0; i < length; i++) {
                char charType = columnName.charAt(i);
                if (Character.isUpperCase(charType)) {
                    builder.append(String.format("_%s", Character.toLowerCase(charType)));
                } else {
                    builder.append(charType);
                }
            }
            return builder.toString();
        }).collect(Collectors.toList());
    }

    /**
     * 获取实体映射的列名列表,遵守java bean规范
     *
     * @param clazz 实体class
     * @param <T>   class类型
     * @return 映射的表中列名列表
     */
    public static <T extends Serializable> List<String> findColumnNames(Class<T> clazz) {
        Set<String> methods = Stream.of(clazz.getMethods())
                .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
                .map(Method::getName).collect(Collectors.toSet());


        return Stream.of(clazz.getDeclaredFields()).filter(field -> {
            char[] fieldNames = field.getName().toCharArray();
            fieldNames[0] -= 32;
            String name = String.valueOf(fieldNames);

            return methods.contains(String.format("get%s", name)) || methods.contains(String.format("is%s", name));
        }).map(Field::getName).collect(Collectors.toList());
    }
}

