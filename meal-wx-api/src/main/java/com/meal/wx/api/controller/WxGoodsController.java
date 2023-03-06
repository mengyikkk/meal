package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxLittleCalamityVo;
import com.meal.wx.api.service.WxGoodsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/goods")
public class WxGoodsController {

    @Resource
    private WxGoodsService wxGoodsService;

    @PostMapping
    public Result<?> goodsList(@RequestBody WxGoodsVo vo){
        return  this.wxGoodsService.goodsList(vo);
    }

    @PostMapping("/little")
    public Result<?> littleList(@RequestBody WxLittleCalamityVo vo){
        return  this.wxGoodsService.littleCalamityList(vo);
    }
}
