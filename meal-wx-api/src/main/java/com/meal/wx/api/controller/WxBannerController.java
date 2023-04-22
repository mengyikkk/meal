package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.wx.api.service.WxBannerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/banner")
public class WxBannerController {
    @Resource
    private WxBannerService wxBannerService;

    @GetMapping
    public Result<?> banner(@RequestParam(required = false) Long shopId) {
        return this.wxBannerService.list(shopId);
    }
}
