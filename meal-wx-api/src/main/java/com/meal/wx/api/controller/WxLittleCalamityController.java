package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.WxLittleCalamityVo;
import com.meal.wx.api.service.WxGoodsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/littleCalamity")
public class WxLittleCalamityController {
    @Resource
    private WxGoodsService wxGoodsService;
    @PostMapping
    public Result<?> littleCalamityList(@RequestBody WxLittleCalamityVo vo){
        return  this.wxGoodsService.littleCalamityList(vo);
    }
}
