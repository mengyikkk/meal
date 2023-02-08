package com.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.utils.SecurityUtils;
import com.meal.common.model.ShopRequestVo;
import com.wx.api.service.WxShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/shop")
@Validated
public class WxShopController {
    private final Logger logger = LoggerFactory.getLogger(WxShopController.class);

    @Resource
    private WxShopService wxShopService;
    @PostMapping("/list")
    public Result<?> list(@RequestBody (required = false)ShopRequestVo request) {
        this.logger.info("user:{},查询店铺",SecurityUtils.getUserId());
        return this.wxShopService.shop(request);
    }
}
