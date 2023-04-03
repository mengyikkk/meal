package com.meal.common.dto;

import com.meal.common.model.OrderCartVo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class WxOrderVo {
    @NotNull
    private Long shopId; //店铺id
    private String message; // 订单备注

    @NotEmpty
    private List<@Valid OrderCartVo> goods;//购物车列表
    @NotNull
    private BigDecimal orderPrice; //订单总费用
    @NotNull
    private  BigDecimal actualPrice; // 订单实际费用


    public Long getShopId() {
        return shopId;
    }

    public WxOrderVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }


    public String getMessage() {
        return message;
    }

    public WxOrderVo setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<OrderCartVo> getGoods() {
        return goods;
    }

    public WxOrderVo setGoods(List<OrderCartVo> goods) {
        this.goods = goods;
        return this;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public WxOrderVo setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public WxOrderVo setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
        return this;
    }
}
