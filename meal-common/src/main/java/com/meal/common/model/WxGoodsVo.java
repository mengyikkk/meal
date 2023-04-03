package com.meal.common.model;

import javax.validation.constraints.NotNull;

public class WxGoodsVo {
    private Long goodId;
    private String goodsSn;
    @NotNull
    private Long shopId;

    private String goodsName;

    private  Long categoryId;


    private Boolean isOnSale;
    private Integer isTimeOnSale;
    private Integer sortOrder;

    private Integer page;
    private Integer limit;

    public Long getGoodId() {
        return goodId;
    }

    public WxGoodsVo setGoodId(Long goodId) {
        this.goodId = goodId;
        return this;
    }

    public Long getShopId() {
        return shopId;
    }

    public WxGoodsVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public WxGoodsVo setGoodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public WxGoodsVo setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }


    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public WxGoodsVo setIsOnSale(Boolean onSale) {
        isOnSale = onSale;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxGoodsVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public WxGoodsVo setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public WxGoodsVo setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public WxGoodsVo setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public WxGoodsVo setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
        return this;
    }

}
