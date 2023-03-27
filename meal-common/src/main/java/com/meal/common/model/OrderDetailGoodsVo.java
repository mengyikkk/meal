package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailGoodsVo {
    private  String goodsName;

    private  Long  goodsNumber;

    private  String unit;

    private BigDecimal goodsMoney;

    private List<OrderDetailCalamityVo> orderDetailCalamityVos;

    public String getGoodsName() {
        return goodsName;
    }

    public OrderDetailGoodsVo setGoodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }

    public Long getGoodsNumber() {
        return goodsNumber;
    }

    public OrderDetailGoodsVo setGoodsNumber(Long goodsNumber) {
        this.goodsNumber = goodsNumber;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public OrderDetailGoodsVo setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getGoodsMoney() {
        return goodsMoney;
    }

    public OrderDetailGoodsVo setGoodsMoney(BigDecimal goodsMoney) {
        this.goodsMoney = goodsMoney;
        return this;
    }

    public List<OrderDetailCalamityVo> getOrderDetailCalamityVos() {
        return orderDetailCalamityVos;
    }

    public OrderDetailGoodsVo setOrderDetailCalamityVos(List<OrderDetailCalamityVo> orderDetailCalamityVos) {
        this.orderDetailCalamityVos = orderDetailCalamityVos;
        return this;
    }
}
