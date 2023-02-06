package com.wx.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealUser;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.IpUtil;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.TokenUtils;
import com.wx.api.model.WxRegisterVo;
import com.wx.api.dto.UserInfo;
import com.wx.api.dto.WxLoginInfo;
import com.wx.api.service.WxAuthService;
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

//    @Resource
//    private NotifyService notifyService;
    @Resource
    private MealUserMapper mealUserMapper;

    @Resource
    private WxAuthService wxAuthService;

//    @PostMapping("/captcha")
//    public Result<?> captcha(@LoginUser Integer userId,@RequestBody CaptchaVo vo) {
//        if(userId == null){
//            return ResultUtils.unknown();
//        }
//        String phoneNumber = vo.getMobile();
//        String captchaType = vo.getType();
//        if (StringUtils.isBlank(phoneNumber)) {
//            return ResultUtils.unknown();
//        }
//        if (!RegexUtil.isMobileSimple(phoneNumber)) {
//            return ResultUtils.code(ResponseCode.AUTH_INVALID_MOBILE);
//        }
//        if (StringUtils.isEmpty(captchaType)) {
//            return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
//        }
//
//        if (!notifyService.isSmsEnable()) {
//            return ResultUtils.code(ResponseCode.AUTH_CAPTCHA_UNSUPPORT);
//        }
//        String code = CharUtil.getRandomNum(6);
//        boolean successful = CaptchaCodeManager.addToCache(phoneNumber, code);
//        if (!successful) {
//            return ResultUtils.code(ResponseCode.AUTH_CAPTCHA_FREQUENCY);
//        }
//        notifyService.notifySmsTemplate(phoneNumber, NotifyType.CAPTCHA, new String[]{code});
//        return ResultUtils.success();
//    }
    /**
     * 目前用手机号当用户名
     * @param vo
     * @param request
     * @return
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody WxRegisterVo vo, HttpServletRequest request) {
        return  this.wxAuthService.register(vo,request);
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
