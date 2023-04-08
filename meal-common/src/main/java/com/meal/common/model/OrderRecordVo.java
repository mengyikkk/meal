package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderRecordVo {
    private String orderSn;
    private String shopName;
    private String shopAvatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private  Short  orderStatus;
    private String orderStatusMessage;
    private BigDecimal money;

    private String shipSnByBreakFast;
    private String shipSnByLunch;
    private String shipSnByDinner;

    public String getOrderSn() {
        return orderSn;
    }

    public OrderRecordVo setOrderSn(String orderSn) {
        this.orderSn = orderSn;
        return this;
    }

    public String getShopName() {
        return shopName;
    }

    public OrderRecordVo setShopName(String shopName) {
        this.shopName = shopName;
        return this;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public OrderRecordVo setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderRecordVo setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public Short getOrderStatus() {
        return orderStatus;
    }

    public OrderRecordVo setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public String getOrderStatusMessage() {
        return orderStatusMessage;
    }

    public OrderRecordVo setOrderStatusMessage(String orderStatusMessage) {
        this.orderStatusMessage = orderStatusMessage;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public OrderRecordVo setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public String getShipSnByBreakFast() {
        return shipSnByBreakFast;
    }

    public OrderRecordVo setShipSnByBreakFast(String shipSnByBreakFast) {
        this.shipSnByBreakFast = shipSnByBreakFast;
        return this;
    }

    public String getShipSnByLunch() {
        return shipSnByLunch;
    }

    public OrderRecordVo setShipSnByLunch(String shipSnByLunch) {
        this.shipSnByLunch = shipSnByLunch;
        return this;
    }

    public String getShipSnByDinner() {
        return shipSnByDinner;
    }

    public OrderRecordVo setShipSnByDinner(String shipSnByDinner) {
        this.shipSnByDinner = shipSnByDinner;
        return this;
    }
}
