package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailsVo {
    private String nickName;

    private String orderSn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private List<OrderDetailGoodsVo> orderDetailGoodsVos;

    private Long count;

    private BigDecimal money;

    private  String shopName;

    private String shopPhone;

    public String getShopName() {
        return shopName;
    }

    public OrderDetailsVo setShopName(String shopName) {
        this.shopName = shopName;
        return this;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public OrderDetailsVo setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public OrderDetailsVo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public OrderDetailsVo setOrderSn(String orderSn) {
        this.orderSn = orderSn;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderDetailsVo setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public List<OrderDetailGoodsVo> getOrderDetailGoodsVos() {
        return orderDetailGoodsVos;
    }

    public OrderDetailsVo setOrderDetailGoodsVos(List<OrderDetailGoodsVo> orderDetailGoodsVos) {
        this.orderDetailGoodsVos = orderDetailGoodsVos;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public OrderDetailsVo setCount(Long count) {
        this.count = count;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public OrderDetailsVo setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }
}
