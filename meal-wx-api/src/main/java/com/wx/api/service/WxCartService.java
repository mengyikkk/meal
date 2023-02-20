package com.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.WxShoppingCartVo;

import java.util.List;

public interface WxCartService {

     Result<?> insertRqSuperShoppingCart(WxShoppingCartVo shoppingCartVo, Long uid);
}
