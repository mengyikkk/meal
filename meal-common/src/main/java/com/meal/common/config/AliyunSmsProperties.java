package com.meal.common.config;


import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

public class AliyunSmsProperties {
    private  String sign;
    private  String templateCode;
    @NestedConfigurationProperty
    private AliyunProperties aliyun;

    public String getSign() {
        return sign;
    }

    public AliyunSmsProperties setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public AliyunSmsProperties setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        return this;
    }

    public AliyunProperties getAliyun() {
        return aliyun;
    }

    public AliyunSmsProperties setAliyun(AliyunProperties aliyun) {
        this.aliyun = aliyun;
        return this;
    }
}
