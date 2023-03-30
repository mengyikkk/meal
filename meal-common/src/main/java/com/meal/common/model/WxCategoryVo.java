package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class WxCategoryVo {
    private Long id;
    private String name;

    private Integer isTimeOnSale;

    private Long parentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<WxCategoryVo> categoryVos;

    public Long getId() {
        return id;
    }

    public WxCategoryVo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WxCategoryVo setName(String name) {
        this.name = name;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public WxCategoryVo setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public List<WxCategoryVo> getCategoryVos() {
        return categoryVos;
    }

    public WxCategoryVo setCategoryVos(List<WxCategoryVo> categoryVos) {
        this.categoryVos = categoryVos;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxCategoryVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
