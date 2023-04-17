package com.meal.wx.api.controller;

import com.meal.common.Result;
import com.meal.common.model.*;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxAuthService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wx/auth")
public class WxAuthController {


    @Resource
    private WxAuthService wxAuthService;


    /**
     * 目前用手机号当用户名
     *
     * @param vo
     * @param request
     * @return
     */
    @PostMapping("/register")
    public Result<WxRegisterReturnVo> register(@RequestBody WxRegisterVo vo, HttpServletRequest request) {
        return this.wxAuthService.register(vo, request);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginVo vo, HttpServletRequest request) {
        return this.wxAuthService.login(vo, request);
    }

    @PostMapping("/sms")
    public Result<?> sms(String phoneNumber) {
        return this.wxAuthService.sms(phoneNumber);
    }

    @PostMapping("/refresh")
    public Result<?> refresh(@RequestHeader("${jwt.tokenHeader}") String token) {
        return this.wxAuthService.refresh(token);
    }

    @GetMapping("/bind/{shopId}")
    public Result<?> bind(@PathVariable("shopId") Long shopId) {
        return this.wxAuthService.bind(shopId);
    }

    @PostMapping("/loginByWx")
    public Result<?> loginByWx(@RequestBody WxLoginVo info, HttpServletRequest request) {
        return this.wxAuthService.loginByWx(info, request);
    }
    @GetMapping("/bind")
    public Result<?> getShopId() {
        return this.wxAuthService.getShopId(SecurityUtils.getUserId());
    }
    @PostMapping("/update")
    public Result<?> update(@RequestBody UserDetailsVo info) {
        return this.wxAuthService.update(info, SecurityUtils.getUserId());
    }
    @GetMapping("/userInfo")
    public Result<?> info() {
        return this.wxAuthService.info(SecurityUtils.getUserId());
    }
}
