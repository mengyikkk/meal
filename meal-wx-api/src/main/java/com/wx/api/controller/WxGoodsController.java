package com.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxLittleCalamityVo;
import com.wx.api.service.WxGoodsService;
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

}
