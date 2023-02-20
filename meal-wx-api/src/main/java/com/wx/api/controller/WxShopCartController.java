package com.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.ShoppingCartVo;
import com.meal.common.model.WxShoppingCartVo;
import com.meal.common.utils.SecurityUtils;
import com.wx.api.service.WxCartService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("/v1/cart")
public class WxShopCartController {

    @Resource
    private WxCartService wxCartService;

    @PutMapping("/add")
    public Result<?> add(@Valid @RequestBody WxShoppingCartVo shoppingCart)
    {
        return wxCartService.insertRqSuperShoppingCart(shoppingCart,SecurityUtils.getUserId());
    }
}
