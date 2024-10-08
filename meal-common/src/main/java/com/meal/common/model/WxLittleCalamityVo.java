package com.meal.common.model;

import javax.validation.constraints.NotNull;

public class WxLittleCalamityVo {

    private Long calamityId;
    private Long goodsId;
    @NotNull
    private Long shopId;

    private String calamityName;

    private  Long categoryId;


    private Boolean isOnSale;

    private Integer isTimeOnSale;
    private Integer sortOrder;

    private Integer page;
    private Integer limit;

    public Long getCalamityId() {
        return calamityId;
    }

    public WxLittleCalamityVo setCalamityId(Long calamityId) {
        this.calamityId = calamityId;
        return this;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public WxLittleCalamityVo setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
        return this;
    }



    public Long getShopId() {
        return shopId;
    }

    public WxLittleCalamityVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public String getCalamityName() {
        return calamityName;
    }

    public WxLittleCalamityVo setCalamityName(String calamityName) {
        this.calamityName = calamityName;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public WxLittleCalamityVo setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public WxLittleCalamityVo setIsOnSale(Boolean onSale) {
        isOnSale = onSale;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxLittleCalamityVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public WxLittleCalamityVo setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public WxLittleCalamityVo setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public WxLittleCalamityVo setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
