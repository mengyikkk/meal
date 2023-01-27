package com.wx.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealUser;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.IpUtil;
import com.meal.common.utils.RegexUtil;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.bcrypt.BCryptPasswordEncoder;
import com.wx.api.WxRegisterVo;
import com.wx.api.dto.UserInfo;
import com.wx.api.dto.WxLoginInfo;
import com.wx.api.service.UserTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
    private Validator validator;
    @Resource
    private MealUserMapper mealUserMapper;
    @PostMapping("register")
    public Object register(@RequestBody WxRegisterVo vo, HttpServletRequest request) {
        {
            Set<ConstraintViolation<WxRegisterVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Store[trade][block]:param.  request: {}, violation: {}",
                        vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var mobile =vo.getMobile();
        // 如果是小程序注册，则必须非空
        // 其他情况，可以为空
        String wxCode = vo.getWxCode();
        var user = new MealUser();
        var oldUser = mealUserService.queryByMobile(mobile);
        if (Objects.nonNull(user)){
            return ResultUtils.code(ResponseCode.AUTH_MOBILE_REGISTERED);
        }

        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResultUtils.code(ResponseCode.AUTH_INVALID_MOBILE);
        }
/*        //判断验证码是否正确
        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code)) {
            return ResultUtils.code(AUTH_CAPTCHA_UNMATCH, "验证码错误");
        }*/

/*        String openId = "";
        // 非空，则是小程序注册
        // 继续验证openid
        if(!StringUtils.isEmpty(wxCode)) {
            try {
                WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(wxCode);
                openId = result.getOpenid();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseUtil.fail(AUTH_OPENID_UNACCESS, "openid 获取失败");
            }
            userList = userService.queryByOpenid(openId);
            if (userList.size() > 1) {
                return ResponseUtil.serious();
            }
            if (userList.size() == 1) {
                LitemallUser checkUser = userList.get(0);
                String checkUsername = checkUser.getUsername();
                String checkPassword = checkUser.getPassword();
                if (!checkUsername.equals(openId) || !checkPassword.equals(openId)) {
                    return ResponseUtil.fail(AUTH_OPENID_BINDED, "openid已绑定账号");
                }
            }
        }*/

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(vo.getPassword());
//        user.setUsername(vo.getUsername());
//        user.setPassword(encodedPassword);
//        user.setMobile(mobile);
//        user.setWeixinOpenid(openId);
//        user.setAvatar("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
//        user.setNickname(username);
//        user.setGender((byte) 0);
//        user.setUserLevel((byte) 0);
//        user.setStatus((byte) 0);
//        user.setLastLoginTime(LocalDateTime.now());
//        user.setLastLoginIp(IpUtil.getIpAddr(request));
//        userService.add(user);
//
//        // 给新用户发送注册优惠券
//        couponAssignService.assignForRegister(user.getId());
//
//        // userInfo
//        UserInfo userInfo = new UserInfo();
//        userInfo.setNickName(username);
//        userInfo.setAvatarUrl(user.getAvatar());
//
//        // token
//        String token = UserTokenManager.generateToken(user.getId());
//
//        Map<Object, Object> result = new HashMap<Object, Object>();
//        result.put("token", token);
//        result.put("userInfo", userInfo);
        return ResultUtils.success();
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
        String token = UserTokenManager.generateToken(user.getId());
        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return  ResultUtils.success(result);
    }
}
