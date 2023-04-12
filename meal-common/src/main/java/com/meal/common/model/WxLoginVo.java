package com.meal.common.model;

import javax.validation.constraints.NotBlank;

public class WxLoginVo {
    @NotBlank
    private String code;

    private String codePhone;

    @NotBlank
    private String encryptedData;
    @NotBlank
    private String iv;


    public String getCode() {
        return code;
    }

    public WxLoginVo setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCodePhone() {
        return codePhone;
    }

    public WxLoginVo setCodePhone(String codePhone) {
        this.codePhone = codePhone;
        return this;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public WxLoginVo setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
        return this;
    }

    public String getIv() {
        return iv;
    }

    public WxLoginVo setIv(String iv) {
        this.iv = iv;
        return this;
    }
}
