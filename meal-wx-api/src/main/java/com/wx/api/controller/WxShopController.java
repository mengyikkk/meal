package com.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.config.SecurityConfig;
import com.wx.api.service.WxShopService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wx/shop")
@Validated
public class WxShopController {
    private final Log logger = LogFactory.getLog(WxShopController.class);

    @Resource
    private WxShopService wxShopService;
    @PostMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        return this.wxShopService.shop(page,limit);
    }
}
