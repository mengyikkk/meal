package com.wx.api.model;

import javax.validation.constraints.NotBlank;

public class LoginVo {
    private String password;
    @NotBlank
    private String phoneNumber;
    private String code;

//    "1、账号密码登录，2、手机验证码登录"
    @NotBlank
    private String type;

    public String getPassword() {
        return password;
    }

    public LoginVo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LoginVo setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getCode() {
        return code;
    }

    public LoginVo setCode(String code) {
        this.code = code;
        return this;
    }

    public String getType() {
        return type;
    }

    public LoginVo setType(String type) {
        this.type = type;
        return this;
    }
}
