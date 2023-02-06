package com.wx.api.model;

import javax.validation.constraints.NotBlank;

public class WxRegisterVo {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String nickname;
    @NotBlank
    private String mobile;
    @NotBlank
    private String code;
    private String wxCode;

    public String getUsername() {
        return username;
    }

    public WxRegisterVo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public WxRegisterVo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public WxRegisterVo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getCode() {
        return code;
    }

    public WxRegisterVo setCode(String code) {
        this.code = code;
        return this;
    }

    public String getWxCode() {
        return wxCode;
    }

    public WxRegisterVo setWxCode(String wxCode) {
        this.wxCode = wxCode;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public WxRegisterVo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
