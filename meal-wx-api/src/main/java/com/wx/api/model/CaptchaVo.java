package com.wx.api.model;

public class CaptchaVo {

    private  String mobile;

    private  String type;

    public String getMobile() {
        return mobile;
    }

    public CaptchaVo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getType() {
        return type;
    }

    public CaptchaVo setType(String type) {
        this.type = type;
        return this;
    }
}
