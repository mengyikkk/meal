package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.dto.WxOrderVo;
import com.meal.common.model.WxShoppingCartVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WxOrderService {
    Result<?> submit(WxOrderVo wxOrderVo, Long uid);
    Result<?> prepay(Long uid, String orderSn ,String message);
    Result<?> refund(Long uid,String orderSn);
    Result<?> detail(Long uid,String orderSn,Integer page ,Integer limit);
    Object payNotify(HttpServletRequest request, HttpServletResponse response);
    Object refundNotify(String xmlData);
}
