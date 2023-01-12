package com.meal.common;


import com.meal.common.utils.EntityNameMappingUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class DefaultSortPagination implements SortPagination {

    private Integer page = 1;
    private Integer limit = 10;
    private List<String> sorts =  Collections.emptyList();

    public static DefaultSortPagination ofLimit(Integer limit) {
        return of(1, limit);
    }

    public static DefaultSortPagination of(Integer page, Integer limit) {
        DefaultSortPagination pagination = new DefaultSortPagination();
        pagination.setPage(page);
        pagination.setLimit(limit);
        return pagination;
    }

    @Override
    public Integer page() {
        return this.page;
    }

    @Override
    public Integer limit() {
        return this.limit;
    }

    @Override
    public Long offset() {
        return ((long) (this.page - 1) * this.limit);
    }

    @Override
    public List<String> sorts() {
        return this.sorts;
    }

    @Override
    public <T extends Serializable> void refreshSorts(Class<T> clazz) {
        this.sorts = List.copyOf(EntityNameMappingUtils.orderColumnNameUnderscoreToCameCase(clazz, this.sorts));
    }

    @Override
    public String toString() {
        return String.format("[page = %d, limit = %d, offset = %d, sort = %s]",
                this.page, this.limit, this.offset(), this.sorts);
    }

    public Integer getPage() {
        return page;
    }

    public DefaultSortPagination setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public DefaultSortPagination setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public List<String> getSorts() {
        return sorts;
    }

    public DefaultSortPagination setSorts(List<String> sorts) {
        this.sorts = sorts;
        return this;
    }

    public Long getOffset() {
        return this.offset();
    }
}
