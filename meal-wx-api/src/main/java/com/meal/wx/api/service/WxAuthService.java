package com.meal.wx.api.service;

import com.meal.common.Result;
import com.meal.common.model.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface WxAuthService {
     Result<WxRegisterReturnVo> register(WxRegisterVo vo, HttpServletRequest request);
     Result<?> loginByWx(WxLoginVo info, HttpServletRequest request);
     Result<?> login(LoginVo vo, HttpServletRequest request);
     Result<?> sms(String phoneNumber);
     Result<?> refresh(String token);
     Result<?> bind(Long shopId);
     Result<?> getShopId(Long userId);
     Result<?> update(UserDetailsVo vo, Long userId);
     Result<?> info(Long userId);
     Result<?> send(LocalDateTime shipTime , Integer isTimeOnSale);

}
