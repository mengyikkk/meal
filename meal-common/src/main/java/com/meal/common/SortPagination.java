package com.meal.common;

import java.io.Serializable;
import java.util.List;

public interface SortPagination {

    Integer page();

    Integer limit();

    Long offset();

    /**
     * 排序字段列表,按在列表中的顺序决定排序优先级
     * @return 排序字段列表
     */
    List<String> sorts();

    /**
     * 对排序字段进行一次实体匹配,通常情况下{@link #sorts()}中的数据都是没有被过滤的数据，它可能包含非法的或是无效的字段名称。
     * 这个操作是将这些字段名称与标准的数据实体class进行一次匹配，过滤掉无效的字段，并且可以进行其它操作，如根据数据库实现以及自定义规则
     * 为字段添加升序和降序关键字
     * @param clazz 数据映射实体，它应当遵守java bean规范以及数据库列表映射约定
     * @param <T> 数据映射类型
     */
    <T extends Serializable> void refreshSorts(Class<T> clazz);
}
