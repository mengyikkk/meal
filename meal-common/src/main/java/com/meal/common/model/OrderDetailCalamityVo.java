package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailCalamityVo {
    private  String calamityName;

    private  Long  calamityNumber;

    private  String unit;

    private BigDecimal calamityMoney;

    public String getCalamityName() {
        return calamityName;
    }

    public OrderDetailCalamityVo setCalamityName(String calamityName) {
        this.calamityName = calamityName;
        return this;
    }

    public Long getCalamityNumber() {
        return calamityNumber;
    }

    public OrderDetailCalamityVo setCalamityNumber(Long calamityNumber) {
        this.calamityNumber = calamityNumber;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public OrderDetailCalamityVo setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BigDecimal getCalamityMoney() {
        return calamityMoney;
    }

    public OrderDetailCalamityVo setCalamityMoney(BigDecimal calamityMoney) {
        this.calamityMoney = calamityMoney;
        return this;
    }
}
