package com.meal.common.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class WxShoppingCartVo {
    @NotNull
    private  Long shopId;
    @NotEmpty
    private List<ShoppingCartVo> goods;

    public Long getShopId() {
        return shopId;
    }

    public WxShoppingCartVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public List<ShoppingCartVo> getGoods() {
        return goods;
    }

    public WxShoppingCartVo setGoods(List<ShoppingCartVo> goods) {
        this.goods = goods;
        return this;
    }
}
