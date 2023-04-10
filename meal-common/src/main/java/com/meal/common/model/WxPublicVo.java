package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class WxPublicVo {
    @NotNull
    private  Long shopId;
    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shipDate;
    @NotNull
    private Integer isTimeOnSale;

    public Long getShopId() {
        return shopId;
    }

    public WxPublicVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public WxPublicVo setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxPublicVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
