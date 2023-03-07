package com.meal.wx.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealUser;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.model.WxRegisterReturnVo;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.IpUtil;
import com.meal.common.utils.ResultUtils;
import com.meal.common.model.LoginVo;
import com.meal.common.model.WxRegisterVo;
import com.meal.common.dto.UserInfo;
import com.meal.wx.api.dto.WxLoginInfo;
import com.meal.wx.api.service.WxAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
@RestController
@RequestMapping("/wx/auth")
public class WxAuthController {
    private final Logger logger = LoggerFactory.getLogger(WxAuthController.class);
//    @Resource
    private WxMaService wxService;

    @Resource
    private MealUserService mealUserService;

    @Resource
    private MealUserMapper mealUserMapper;

    @Resource
    private WxAuthService wxAuthService;


    /**
     * 目前用手机号当用户名
     * @param vo
     * @param request
     * @return
     */
    @PostMapping("/register")
    public Result<WxRegisterReturnVo> register(@RequestBody WxRegisterVo vo, HttpServletRequest request) {
        return  this.wxAuthService.register(vo,request);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginVo vo, HttpServletRequest request){
        return  this.wxAuthService.login(vo,request);
    }

    @PostMapping("/sms")
    public Result<?> sms(String phoneNumber){
        return  this.wxAuthService.sms(phoneNumber);
    }
    @PostMapping("/refresh")
    public Result<?> refresh(@RequestHeader("${jwt.tokenHeader}") String token){
        return  this.wxAuthService.refresh(token);
    }
    @GetMapping("/bind/{shopId}")
    public Result<?> bind(@PathVariable("shopId") Long shopId ){
        return  this.wxAuthService.bind(shopId);
    }

    @PostMapping("/loginByWx")
    public Result<?> loginByWx(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (Objects.isNull(code) || Objects.isNull(userInfo)) {
            return null;
        }
        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sessionKey == null || openId == null) {
            //用户为空
            return ResultUtils.unknown();
        }
        MealUser user = mealUserService.queryByOid(openId);
        if (Objects.isNull(user)){
            user = new MealUser();
            user.setUsername(openId);
            user.setPassword(openId);
            user.setWxOpenid(openId);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setUserLevel((byte) 0);
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setSessionKey(sessionKey);
            this.mealUserService.add(user);
        }
        else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            if (this.mealUserMapper.updateByPrimaryKey(user) == 0) {
                return ResultUtils.code(ResponseCode.ACCESS_LIMIT);
            }
        }
        // token
//        String token = TokenUtils.generateToken(user.getId());
        Map<Object, Object> result = new HashMap<Object, Object>();
//        result.put("token", token);
//        result.put("userInfo", userInfo);
        return  ResultUtils.success(result);
    }
}
