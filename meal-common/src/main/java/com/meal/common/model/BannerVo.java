package com.meal.common.model;

public class BannerVo {
    private String description;

    private String bannerUrl;

    private Byte orderNum;

    private String name;

    public String getDescription() {
        return description;
    }

    public BannerVo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public BannerVo setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        return this;
    }

    public Byte getOrderNum() {
        return orderNum;
    }

    public BannerVo setOrderNum(Byte orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public String getName() {
        return name;
    }

    public BannerVo setName(String name) {
        this.name = name;
        return this;
    }
}
