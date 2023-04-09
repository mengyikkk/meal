package com.meal.common.model;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailSonVo {
    private List<OrderDetailGoodsVo> orderDetailGoodsVos;

    private Long count;

    private BigDecimal money;

    private String shipSn;

    private Integer  isTimeOnSale;

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

    public String getShipSn() {
        return shipSn;
    }

    public OrderDetailSonVo setShipSn(String shipSn) {
        this.shipSn = shipSn;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public OrderDetailSonVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
