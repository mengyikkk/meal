package com.meal.common.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class OrderCartCalamityVo {
    private Long calamityId;
    @Positive
    private Long calamityNumber;
    private BigDecimal price;

    public Long getCalamityId() {
        return calamityId;
    }

    public OrderCartCalamityVo setCalamityId(Long calamityId) {
        this.calamityId = calamityId;
        return this;
    }

    public Long getCalamityNumber() {
        return calamityNumber;
    }

    public OrderCartCalamityVo setCalamityNumber(Long calamityNumber) {
        this.calamityNumber = calamityNumber;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderCartCalamityVo setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
