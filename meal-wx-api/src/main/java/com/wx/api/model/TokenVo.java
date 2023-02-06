package com.wx.api.model;

public class TokenVo {
    private  String token;

    private  String refresh;

    private  Long expire;

    public String getToken() {
        return token;
    }

    public TokenVo setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefresh() {
        return refresh;
    }

    public TokenVo setRefresh(String refresh) {
        this.refresh = refresh;
        return this;
    }

    public Long getExpire() {
        return expire;
    }

    public TokenVo setExpire(Long expire) {
        this.expire = expire;
        return this;
    }
}
