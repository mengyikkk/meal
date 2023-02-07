package com.wx.api.service;

import com.meal.common.Result;
import com.wx.api.model.LoginVo;
import com.wx.api.model.WxRegisterVo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface WxAuthService {
     Result<?> register(WxRegisterVo vo, HttpServletRequest request);
     Result<?> login(LoginVo vo, HttpServletRequest request);
     Result<?> sms(String phoneNumber);
     Result<?> refresh(String token);
}
