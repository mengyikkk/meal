package com.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.WxShoppingCartVo;

public interface WxCartService {

     Result<?> insertShoppingCart(WxShoppingCartVo shoppingCartVo, Long uid);
     Result<?> selectShoppingCartAmount(Long uid, Long shopId);
     Result<?> selectShoppingCartList(Long uid, Long shopId);
     Result<?> deleteShoppingCartList(Long uid, Long shopId);
}
