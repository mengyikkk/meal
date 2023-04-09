package com.meal.common.model;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class WxShoppingCartOrderVo {
    @NotEmpty
    private List<WxShopCartResponseVo> goods;
    private Integer isTimeOnSale;

    public List<WxShopCartResponseVo> getGoods() {
        return goods;
    }

    public WxShoppingCartOrderVo setGoods(List<WxShopCartResponseVo> goods) {
        this.goods = goods;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxShoppingCartOrderVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
