package com.meal.common;

public interface StateMapping <T extends Comparable<T>>{

    /**
     * 获取映射值
     * @return 映射关系值
     */
    T getMapping();

    /**
     * 查看是否具有映射关系
     * @param t 映射关系类型值
     * @return true 具有映射关系，false 不具有映射关系
     */
    default boolean is(T t){
        return getMapping().compareTo(t) == 0;
    }

    /**
     * 查看是否不具有映射关系
     * @param t 映射关系类型值
     * @return true 不具有映射关系，false 具有映射关系
     */
    default boolean not(T t){
        return !is(t);
    }
}
