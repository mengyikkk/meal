package com.wx.api.model;


import com.wx.api.dto.UserInfo;

public class WxRegisterReturnVo {
    private UserInfo userInfo;

    private String token;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public WxRegisterReturnVo setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public String getToken() {
        return token;
    }

    public WxRegisterReturnVo setToken(String token) {
        this.token = token;
        return this;
    }
}
