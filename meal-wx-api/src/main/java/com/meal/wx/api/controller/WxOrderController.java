package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.dto.WxOrderVo;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@RestController
@RequestMapping("/wx/order")
public class WxOrderController {

    @Resource
    private WxOrderService wxOrderService;
    @PostMapping("/submit")
    public Result<?> submit(@RequestBody WxOrderVo wxOrder){
        return this.wxOrderService.submit(wxOrder, SecurityUtils.getUserId());
    }
}
