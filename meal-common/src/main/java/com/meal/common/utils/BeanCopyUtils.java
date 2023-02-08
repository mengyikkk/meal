package com.meal.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    public BeanCopyUtils() {

    }

    public static <V> V copyBean(Object source, Class<V> clazz) {
        V result = null;
        try {
            result = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <V> List<V> copyBeanList(Collection<?> list, Class<V> clazz) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(e -> copyBean(e, clazz))
                .collect(Collectors.toList());
    }
}
