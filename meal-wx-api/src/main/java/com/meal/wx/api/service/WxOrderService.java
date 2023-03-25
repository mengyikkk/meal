package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.dto.WxOrderVo;
import com.meal.common.model.WxShoppingCartVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WxOrderService {
    Result<?> submit(WxOrderVo wxOrderVo, Long uid);
    Result<?> prepay(Long uid,Long orderId);
    Object payNotify(HttpServletRequest request, HttpServletResponse response);
}
