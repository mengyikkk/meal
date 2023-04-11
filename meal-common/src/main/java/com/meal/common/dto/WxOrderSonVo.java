package com.meal.common.dto;

import com.meal.common.model.OrderCartVo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class WxOrderSonVo {
    @NotEmpty
    private List<@Valid OrderCartVo> goods;//购物车列表

    private Integer isTimeOnSale;

    public List<OrderCartVo> getGoods() {
        return goods;
    }

    public WxOrderSonVo setGoods(List<OrderCartVo> goods) {
        this.goods = goods;
        return this;
    }


    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxOrderSonVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
