package com.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.WxGoodsVo;

public interface WxGoodsService {

    Result<?> goodsList(WxGoodsVo vo);
}
