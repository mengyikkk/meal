package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.dto.WxOrderVo;
import com.meal.common.model.WxShoppingCartVo;

public interface WxOrderService {
    Result<?> submit(WxOrderVo wxOrderVo, Long uid);
}
