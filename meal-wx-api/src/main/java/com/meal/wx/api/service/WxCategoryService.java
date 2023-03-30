package com.meal.wx.api.service;

import com.meal.common.Result;

public interface WxCategoryService {

    Result<?> list(Long uid, Long shopId,Integer isTimeOnSale);
}
