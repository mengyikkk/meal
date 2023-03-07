package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.LoginVo;
import com.meal.common.model.WxRegisterReturnVo;
import com.meal.common.model.WxRegisterVo;

import javax.servlet.http.HttpServletRequest;

public interface WxAuthService {
     Result<WxRegisterReturnVo> register(WxRegisterVo vo, HttpServletRequest request);
     Result<?> login(LoginVo vo, HttpServletRequest request);
     Result<?> sms(String phoneNumber);
     Result<?> refresh(String token);
     Result<?> bind(Long shopId);
}
