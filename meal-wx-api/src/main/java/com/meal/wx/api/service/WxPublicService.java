package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.WxPublicVo;

public interface WxPublicService {

    Result<?> ship(WxPublicVo vo);
}
