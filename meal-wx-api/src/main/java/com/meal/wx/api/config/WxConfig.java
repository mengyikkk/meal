package com.meal.wx.api.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class WxConfig {
    @Resource
    private WxProperties properties;

    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(properties.getAppId());
        config.setSecret(properties.getAppSecret());
        config.setToken(properties.getToken());
        config.setMsgDataFormat(properties.getMsgDataFormat());
        config.setAesKey(properties.getAesKey());
        return config;
    }


    @Bean
    public WxMaService wxMaService(WxMaConfig maConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(maConfig);
        return service;
    }

//    @Bean
//    public WxPayConfig wxPayConfig() {
//        WxPayConfig payConfig = new WxPayConfig();
//        payConfig.setAppId(properties.getAppId());
//        payConfig.setMchId(properties.getMchId());
//        payConfig.setMchKey(properties.getMchKey());
//        payConfig.setNotifyUrl(properties.getNotifyUrl());
//        payConfig.setKeyPath(properties.getKeyPath());
//        payConfig.setTradeType("JSAPI");
//        payConfig.setSignType("MD5");
//        return payConfig;
//    }
//
//
//    @Bean
//    public WxPayService wxPayService(WxPayConfig payConfig) {
//        WxPayService wxPayService = new WxPayServiceImpl();
//        wxPayService.setConfig(payConfig);
//        return wxPayService;
//    }
}
