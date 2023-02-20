package com.meal.common.model;

public class ShoppingCartVo {

    private  Long goodsId;

    private  String goodsSn;


    private  Long number;

    public Long getGoodsId() {
        return goodsId;
    }

    public ShoppingCartVo setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
        return this;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public ShoppingCartVo setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
        return this;
    }


    public Long getNumber() {
        return number;
    }

    public ShoppingCartVo setNumber(Long number) {
        this.number = number;
        return this;
    }
}
