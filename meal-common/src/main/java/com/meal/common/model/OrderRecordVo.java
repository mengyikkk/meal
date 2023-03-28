package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderRecordVo {
    private Long  orderId;
    private String shopName;
    private String shopAvatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private  Short  orderStatus;
    private String orderStatusMessage;
    private BigDecimal money;

    public Long getOrderId() {
        return orderId;
    }

    public OrderRecordVo setOrderId(Long orderId) {
        this.orderId = orderId;
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
}
