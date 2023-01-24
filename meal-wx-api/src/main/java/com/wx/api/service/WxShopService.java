package com.wx.api.service;

import com.meal.common.Result;

import javax.annotation.Resource;

public interface WxShopService {

     Result<?> shop(Integer page, Integer limit);
}
