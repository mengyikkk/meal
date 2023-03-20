package com.meal.common.model;

import javax.validation.constraints.Positive;

public class CartCalamityVo {
    private Long calamityId;
    @Positive
    private Long calamityNumber;

    private  String calamityName;

    public Long getCalamityId() {
        return calamityId;
    }

    public CartCalamityVo setCalamityId(Long calamityId) {
        this.calamityId = calamityId;
        return this;
    }

    public Long getCalamityNumber() {
        return calamityNumber;
    }

    public CartCalamityVo setCalamityNumber(Long calamityNumber) {
        this.calamityNumber = calamityNumber;
        return this;
    }

    public String getCalamityName() {
        return calamityName;
    }

    public CartCalamityVo setCalamityName(String calamityName) {
        this.calamityName = calamityName;
        return this;
    }
}
