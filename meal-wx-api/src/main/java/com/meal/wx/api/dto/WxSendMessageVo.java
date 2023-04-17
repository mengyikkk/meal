package com.meal.wx.api.dto;

public class WxSendMessageVo {
    private String touser;

    /*所需下发的订阅模板id*/
    private String template_id;

    /*点击消息后跳转的页面*/
    private String page;

    /*跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版*/
    private String miniprogram_state="developer";

    /*进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN返回值*/
    private String lang="zh_CN";

    /*模板数据，这里定义为object是希望所有的模板都能使用这个消息配置*/
    private Object data;

    public String getTouser() {
        return touser;
    }

    public WxSendMessageVo setTouser(String touser) {
        this.touser = touser;
        return this;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public WxSendMessageVo setTemplate_id(String template_id) {
        this.template_id = template_id;
        return this;
    }

    public String getPage() {
        return page;
    }

    public WxSendMessageVo setPage(String page) {
        this.page = page;
        return this;
    }

    public String getMiniprogram_state() {
        return miniprogram_state;
    }

    public WxSendMessageVo setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public WxSendMessageVo setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public Object getData() {
        return data;
    }

    public WxSendMessageVo setData(Object data) {
        this.data = data;
        return this;
    }
}
