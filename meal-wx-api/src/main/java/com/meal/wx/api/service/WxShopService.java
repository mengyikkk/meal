package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.ShopRequestVo;

public interface WxShopService {

     Result<?> shop(ShopRequestVo request);
}
