package com.meal.common.model;

import javax.validation.constraints.Positive;

public class OrderCartCalamityVo {
    private Long calamityId;
    @Positive
    private Long calamityNumber;

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

}
