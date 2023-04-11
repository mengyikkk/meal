package com.meal.common.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class OrderCartCalamityVo {
    private Long calamityId;
    @Positive
    private Long calamityNumber;
    private BigDecimal calamityPrice;

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

    public BigDecimal getCalamityPrice() {
        return calamityPrice;
    }

    public OrderCartCalamityVo setCalamityPrice(BigDecimal calamityPrice) {
        this.calamityPrice = calamityPrice;
        return this;
    }
}
