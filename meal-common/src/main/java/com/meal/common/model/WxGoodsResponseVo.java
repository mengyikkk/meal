package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WxGoodsResponseVo {
    private Long id;
    private String goodsSn;

    private String name;

    private Boolean isOnSale;

    private Integer isTimeOnSale;

    private String picUrl;

    private String unit;

    private BigDecimal retailPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    private String detail;

    public Long getId() {
        return id;
    }

    public WxGoodsResponseVo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public WxGoodsResponseVo setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
        return this;
    }

    public String getName() {
        return name;
    }

    public WxGoodsResponseVo setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public WxGoodsResponseVo setIsOnSale(Boolean onSale) {
        isOnSale = onSale;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxGoodsResponseVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public WxGoodsResponseVo setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public WxGoodsResponseVo setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public WxGoodsResponseVo setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
        return this;
    }

    public LocalDateTime getAddTime() {
        return addTime;
    }

    public WxGoodsResponseVo setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public WxGoodsResponseVo setDetail(String detail) {
        this.detail = detail;
        return this;
    }
}
