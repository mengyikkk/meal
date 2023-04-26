package com.meal.wx.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = WxProperties.PREFIX)
public class WxProperties {
    public static final String PREFIX = "meal.wx.miniapp.configs";
    /**
     * 设置微信小程序的appid
     */
    private String appId;
    /**
     * 设置微信小程序的Secret
     */
    @NotBlank
    private String appSecret;

    private String mchId;

    private String mchKey;

    private String notifyUrl;
    private String refundNotifyUrl;

    private String keyPath;

    private  String apiV3Key;
    /**
     * 设置微信小程序消息服务器配置的token
     */

    private String token;
    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;

    private String breakfastId;
    private String lunchId;
    private String dinnerId;


    public String getBreakfastId() {
        return breakfastId;
    }

    public WxProperties setBreakfastId(String breakfastId) {
        this.breakfastId = breakfastId;
        return this;
    }

    public String getLunchId() {
        return lunchId;
    }

    public WxProperties setLunchId(String lunchId) {
        this.lunchId = lunchId;
        return this;
    }

    public String getDinnerId() {
        return dinnerId;
    }

    public WxProperties setDinnerId(String dinnerId) {
        this.dinnerId = dinnerId;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public WxProperties setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public WxProperties setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public WxProperties setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getMchKey() {
        return mchKey;
    }

    public WxProperties setMchKey(String mchKey) {
        this.mchKey = mchKey;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public WxProperties setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public WxProperties setKeyPath(String keyPath) {
        this.keyPath = keyPath;
        return this;
    }

    public String getToken() {
        return token;
    }

    public WxProperties setToken(String token) {
        this.token = token;
        return this;
    }

    public String getAesKey() {
        return aesKey;
    }

    public WxProperties setAesKey(String aesKey) {
        this.aesKey = aesKey;
        return this;
    }

    public String getMsgDataFormat() {
        return msgDataFormat;
    }

    public WxProperties setMsgDataFormat(String msgDataFormat) {
        this.msgDataFormat = msgDataFormat;
        return this;
    }

    public String getApiV3Key() {
        return apiV3Key;
    }

    public WxProperties setApiV3Key(String apiV3Key) {
        this.apiV3Key = apiV3Key;
        return this;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public WxProperties setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
        return this;
    }
}
