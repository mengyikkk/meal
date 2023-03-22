package com.meal.common.model;


import com.meal.common.config.TokenVo;
import com.meal.common.dto.UserInfo;

public class WxRegisterReturnVo {
    private UserInfo userInfo;
    private TokenVo token;

    private Long  shopId;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public WxRegisterReturnVo setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public TokenVo getToken() {
        return token;
    }

    public WxRegisterReturnVo setToken(TokenVo token) {
        this.token = token;
        return this;
    }

    public Long getShopId() {
        return shopId;
    }

    public WxRegisterReturnVo setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }
}
