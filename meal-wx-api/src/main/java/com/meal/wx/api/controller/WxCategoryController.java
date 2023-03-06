package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/category")
public class WxCategoryController {

    @Resource
    private WxCategoryService wxCategoryService;

    @GetMapping
    public Result<?> list(@RequestParam Long shopId) {
        return this.wxCategoryService.list(SecurityUtils.getUserId(), shopId);
    }
}
