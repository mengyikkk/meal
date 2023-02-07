package com.meal.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = MealProperties.PREFIX)
public class MealProperties {
    public static final String PREFIX = "meal";

    @NestedConfigurationProperty
    private AliyunSmsProperties sms;

    public AliyunSmsProperties getSms() {
        return sms;
    }

    public MealProperties setSms(AliyunSmsProperties sms) {
        this.sms = sms;
        return this;
    }
}
