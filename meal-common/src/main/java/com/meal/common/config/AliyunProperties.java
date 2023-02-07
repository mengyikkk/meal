package com.meal.common.config;


public class AliyunProperties {
    private  String regionId;

    private  String accessKeyId;

    private  String accessKeySecret;

    public String getRegionId() {
        return regionId;
    }

    public AliyunProperties setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public AliyunProperties setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public AliyunProperties setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }
}
