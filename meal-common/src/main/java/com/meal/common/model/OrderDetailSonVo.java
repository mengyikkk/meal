package com.meal.common.model;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailSonVo {
    private List<OrderDetailGoodsVo> orderDetailGoodsVos;

    private Long count;

    private BigDecimal money;

    public List<OrderDetailGoodsVo> getOrderDetailGoodsVos() {
        return orderDetailGoodsVos;
    }

    public OrderDetailSonVo setOrderDetailGoodsVos(List<OrderDetailGoodsVo> orderDetailGoodsVos) {
        this.orderDetailGoodsVos = orderDetailGoodsVos;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public OrderDetailSonVo setCount(Long count) {
        this.count = count;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public OrderDetailSonVo setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }
}
