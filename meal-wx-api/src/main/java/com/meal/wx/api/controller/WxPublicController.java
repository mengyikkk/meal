package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.WxPublicVo;
import com.meal.wx.api.service.WxPublicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/admin")
public class WxPublicController {
    @Resource
    private WxPublicService wxPublicService;

    @PostMapping("/ship")
    public Result<?> ship(@RequestBody WxPublicVo vo) {
        return this.wxPublicService.ship(vo);
    }
}
