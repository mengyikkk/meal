package com.wx.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.meal.common.Result;
import com.meal.common.dto.MealUser;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.ResultUtils;
import com.wx.api.dto.UserInfo;
import com.wx.api.dto.WxLoginInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/wx/auth")
public class WxAuthController {

//    @Resource
    private WxMaService wxService;

    @Resource
    private MealUserService mealUserService;

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
        return  ResultUtils.unknown();

    }
}
