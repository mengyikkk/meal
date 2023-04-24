package com.meal.wx.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WxSendVo {
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate shipTime;
    Integer isTimeOnSale;

    public LocalDate getShipTime() {
        return shipTime;
    }

    public WxSendVo setShipTime(LocalDate shipTime) {
        this.shipTime = shipTime;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxSendVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
