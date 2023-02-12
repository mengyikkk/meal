package com.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxLittleCalamityVo;

public interface WxGoodsService {

    Result<?> goodsList(WxGoodsVo vo);

    Result<?> littleCalamityList(WxLittleCalamityVo vo);
}
