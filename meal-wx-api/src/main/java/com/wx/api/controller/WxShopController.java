package com.wx.api.controller;

import com.meal.common.Result;
import com.wx.api.service.WxShopService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wx/shop")
@Validated
public class WxShopController {
    private final Log logger = LogFactory.getLog(WxShopController.class);

    @Resource
    private WxShopService wxShopService;
    @GetMapping("list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        return this.wxShopService.shop(page,limit);
    }
}
