package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WxGoodsResponseVo {
    private Long goodsId;
    private String goodsSn;

    private String goodsName;

    private Boolean isOnSale;

    private Integer isTimeOnSale;

    private String picUrl;
    private Long sortOrder;

    private String unit;

    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    private String detail;

    public Long getGoodsId() {
        return goodsId;
    }

    public WxGoodsResponseVo setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
        return this;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public WxGoodsResponseVo setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
        return this;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public WxGoodsResponseVo setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public BigDecimal getPrice() {
        return price;
    }

    public WxGoodsResponseVo setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getSortOrder() {
        return sortOrder;
    }

    public WxGoodsResponseVo setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }
}
