package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxCartResponseAllVo {
    private List<WxShoppingCartOrderVo>orders;

    private List<WxShopCartResponseVo> errGoods;

    private  Long counts;

    public List<WxShoppingCartOrderVo> getOrders() {
        return orders;
    }

    public WxCartResponseAllVo setOrders(List<WxShoppingCartOrderVo> orders) {
        this.orders = orders;
        return this;
    }

    public List<WxShopCartResponseVo> getErrGoods() {
        return errGoods;
    }

    public WxCartResponseAllVo setErrGoods(List<WxShopCartResponseVo> errGoods) {
        this.errGoods = errGoods;
        return this;
    }

    public Long getCounts() {
        return counts;
    }

    public WxCartResponseAllVo setCounts(Long counts) {
        this.counts = counts;
        return this;
    }
}
