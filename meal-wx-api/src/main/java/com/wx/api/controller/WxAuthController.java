package com.wx.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.meal.common.Result;
import com.meal.common.utils.ResultUtils;
import com.wx.api.dto.UserInfo;
import com.wx.api.dto.WxLoginInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wx/auth")
public class WxAuthController {

//    /**
//     * 账号登录
//     *
//     * @param body    请求内容，{ username: xxx, password: xxx }
//     * @param request 请求对象
//     * @return 登录结果
//     */
//    @PostMapping("login")
//    public Result<?> login(@RequestBody String body, HttpServletRequest request) {
//
//    }

    @Resource
    private WxMaService wxService;

    @PostMapping("loginByWx")
    public Result<?> loginByWx(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
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
        return  ResultUtils.unknown();

    }
}
