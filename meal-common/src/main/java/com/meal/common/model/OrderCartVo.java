package com.meal.common.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class OrderCartVo {
    @NotNull
    private  Long goodsId;

    @NotNull
    @Positive
    private  Long number;
    @NotNull
    private BigDecimal price; //单价

    private List<OrderCartCalamityVo> cartCalamityVos;

    public Long getGoodsId() {
        return goodsId;
    }

    public OrderCartVo setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
        return this;
    }


    public Long getNumber() {
        return number;
    }

    public OrderCartVo setNumber(Long number) {
        this.number = number;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderCartVo setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public List<OrderCartCalamityVo> getCartCalamityVos() {
        return cartCalamityVos;
    }

    public OrderCartVo setCartCalamityVos(List<OrderCartCalamityVo> cartCalamityVos) {
        this.cartCalamityVos = cartCalamityVos;
        return this;
    }
}
