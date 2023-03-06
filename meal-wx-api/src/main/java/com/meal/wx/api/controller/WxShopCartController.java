package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.WxShoppingCartVo;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxCartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/wx/cart")
public class WxShopCartController {

    @Resource
    private WxCartService wxCartService;

    @PostMapping("/add")
    public Result<?> add(@Valid @RequestBody WxShoppingCartVo shoppingCart) {
        return wxCartService.insertShoppingCart(shoppingCart, SecurityUtils.getUserId());
    }

    @GetMapping("/amount")
    public Result<?> amount(@RequestParam Long shopId) {
        return wxCartService.selectShoppingCartAmount(SecurityUtils.getUserId(), shopId);
    }

    @GetMapping
    public Result<?> list(@RequestParam Long shopId) {
        return wxCartService.selectShoppingCartList(SecurityUtils.getUserId(), shopId);
    }

    @PostMapping("/delete")
    public Result<?> delete(@RequestParam Long shopId) {
        return wxCartService.deleteShoppingCartList(SecurityUtils.getUserId(), shopId);
    }
}
