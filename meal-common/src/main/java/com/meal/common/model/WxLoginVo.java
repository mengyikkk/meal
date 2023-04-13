package com.meal.common.model;

import javax.validation.constraints.NotBlank;

public class WxLoginVo {
    @NotBlank
    private String code;


    public String getCode() {
        return code;
    }

    public WxLoginVo setCode(String code) {
        this.code = code;
        return this;
    }

}
