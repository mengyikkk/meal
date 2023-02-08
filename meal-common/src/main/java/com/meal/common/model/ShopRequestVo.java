package com.meal.common.model;

public class ShopRequestVo {
    private Integer page;
    private Integer limit;

    private  Long shopId;

    private  String shopName;

    public Integer getPage() {
        return page;
    }

    public ShopRequestVo setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public ShopRequestVo setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Long getShopId() {
        return shopId;
    }

    public ShopRequestVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public String getShopName() {
        return shopName;
    }

    public ShopRequestVo setShopName(String shopName) {
        this.shopName = shopName;
        return this;
    }
}
