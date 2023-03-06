package com.meal.common.model;

import java.math.BigDecimal;

public class WxShopCartCalamitySonVo {
    private Long calamityId;

    private String calamityName;

    private Long calamityNumber;

    private  String calamityUnit;

    private  String calamityUrl;

    private Boolean errStatus;

    private BigDecimal calamityPrice;


    public Long getCalamityId() {
        return calamityId;
    }

    public WxShopCartCalamitySonVo setCalamityId(Long calamityId) {
        this.calamityId = calamityId;
        return this;
    }

    public String getCalamityName() {
        return calamityName;
    }

    public WxShopCartCalamitySonVo setCalamityName(String calamityName) {
        this.calamityName = calamityName;
        return this;
    }

    public Long getCalamityNumber() {
        return calamityNumber;
    }

    public WxShopCartCalamitySonVo setCalamityNumber(Long calamityNumber) {
        this.calamityNumber = calamityNumber;
        return this;
    }

    public String getCalamityUnit() {
        return calamityUnit;
    }

    public WxShopCartCalamitySonVo setCalamityUnit(String calamityUnit) {
        this.calamityUnit = calamityUnit;
        return this;
    }

    public Boolean getErrStatus() {
        return errStatus;
    }

    public WxShopCartCalamitySonVo setErrStatus(Boolean errStatus) {
        this.errStatus = errStatus;
        return this;
    }

    public String getCalamityUrl() {
        return calamityUrl;
    }

    public WxShopCartCalamitySonVo setCalamityUrl(String calamityUrl) {
        this.calamityUrl = calamityUrl;
        return this;
    }

    public BigDecimal getCalamityPrice() {
        return calamityPrice;
    }

    public WxShopCartCalamitySonVo setCalamityPrice(BigDecimal calamityPrice) {
        this.calamityPrice = calamityPrice;
        return this;
    }
}
