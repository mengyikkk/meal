package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

public class WxShopCartResponseVo {
    private Long goodsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_cart.goods_sn
     *
     * @mbg.generated
     */
    private String goodsSn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_cart.goods_is_time
     *
     * @mbg.generated
     */
    private Boolean goodsIsTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_cart.goods_name
     *
     * @mbg.generated
     */
    private String goodsName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_cart.number
     *
     * @mbg.generated
     */
    private Long number;

    private  String url;

    private String unit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meal_cart.checked
     *
     * @mbg.generated
     */
    private Boolean checked;

    private BigDecimal price;
    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private List<WxShopCartCalamitySonVo> calamityVos;

    private Boolean errStatus;

    private Integer isTimeOnSale;

    public Long getGoodsId() {
        return goodsId;
    }

    public WxShopCartResponseVo setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
        return this;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public WxShopCartResponseVo setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
        return this;
    }

    public Boolean getGoodsIsTime() {
        return goodsIsTime;
    }

    public WxShopCartResponseVo setGoodsIsTime(Boolean goodsIsTime) {
        this.goodsIsTime = goodsIsTime;
        return this;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public WxShopCartResponseVo setGoodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }

    public Long getNumber() {
        return number;
    }

    public WxShopCartResponseVo setNumber(Long number) {
        this.number = number;
        return this;
    }

    public Boolean getChecked() {
        return checked;
    }

    public WxShopCartResponseVo setChecked(Boolean checked) {
        this.checked = checked;
        return this;
    }

    public Boolean getErrStatus() {
        return errStatus;
    }

    public WxShopCartResponseVo setErrStatus(Boolean errStatus) {
        this.errStatus = errStatus;
        return this;
    }

    public List<WxShopCartCalamitySonVo> getCalamityVos() {
        return calamityVos;
    }

    public WxShopCartResponseVo setCalamityVos(List<WxShopCartCalamitySonVo> calamityVos) {
        this.calamityVos = calamityVos;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public WxShopCartResponseVo setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WxShopCartResponseVo setUrl(String url) {
        this.url = url;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public WxShopCartResponseVo setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getIsTimeOnSale() {
        return isTimeOnSale;
    }

    public WxShopCartResponseVo setIsTimeOnSale(Integer isTimeOnSale) {
        this.isTimeOnSale = isTimeOnSale;
        return this;
    }
}
