package com.wx.api.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.config.MealProperties;
import com.meal.common.dto.MealUser;
import com.meal.common.enums.LoginTypeEnum;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.*;
import com.wx.api.dto.UserInfo;
import com.wx.api.dto.UserToken;
import com.wx.api.model.LoginVo;
import com.wx.api.model.TokenVo;
import com.wx.api.model.WxRegisterReturnVo;
import com.wx.api.model.WxRegisterVo;
import com.wx.api.service.WxAuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
@Service
public class WxAuthServiceImpl implements WxAuthService {

    private final Logger logger = LoggerFactory.getLogger(WxAuthServiceImpl.class);
    //    @Resource
    private WxMaService wxService;

    @Resource
    private MealUserService mealUserService;

    //    @Resource
//    private NotifyService notifyService;
    @Resource
    private Validator validator;

    @Resource
    private  TokenUtils tokenUtils;
    @Resource
    private MealUserMapper mealUserMapper;
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserDetailsService userDetailsService;
//    @Resource
//    private SmsUtils smsUtils;
    @Resource
    private MealProperties mealProperties;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public Result<?> register(WxRegisterVo vo, HttpServletRequest request)  {
        {
            Set<ConstraintViolation<WxRegisterVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Store[register][block]:param.  request: {}, violation: {}",
                        vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var mobile =vo.getMobile();
        var password = vo.getPassword();

        // 如果是小程序注册，则必须非空
        // 其他情况，可以为空
        String wxCode = vo.getWxCode();
        var user = new MealUser();
//        if (mealUserService.hasByMobile(mobile)){
//            return ResultUtils.code(ResponseCode.AUTH_MOBILE_REGISTERED);
//        }
        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResultUtils.code(ResponseCode.AUTH_INVALID_MOBILE);
        }
        //todo验证码校验
        //判断验证码是否正确
/*        var code = this.redisUtils.getValue(mobile + "sms").toString();
        if (!vo.getCode().equals(code)){
            return  ResultUtils.message(ResponseCode.AUTH_CAPTCHA_UNMATCH,"验证码不匹配");
        }*/
        String openId = "";
        // 非空，则是小程序注册
        // 继续验证openid
        if (Objects.nonNull(wxCode)) {
            try {
                WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(wxCode);
                openId = result.getOpenid();
            } catch (Exception e) {
                e.printStackTrace();
                return ResultUtils.code(ResponseCode.AUTH_OPENID_UNACCESS);
            }
            var checkUser = this.mealUserService.queryByOid(openId);
            String checkPassword = checkUser.getPassword();
            if (!checkPassword.equals(openId)) {
                return ResultUtils.code(ResponseCode.AUTH_OPENID_BINDED);
            }
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(MD5Utils.md5(password));
        user.setUsername(mobile);
        user.setPassword(encodedPassword);
        user.setMobile(mobile);
        user.setWxOpenid(openId);
        user.setAvatar("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
        user.setNickname(vo.getNickname());
        user.setGender((byte) 0);
        user.setUserLevel((byte) 0);
        user.setStatus((byte) 0);
        this.LastLogIn(user,request);
        if (this.mealUserMapper.insertSelective(user)<1){
            return ResultUtils.code(ResponseCode.TIME_OUT);
        }
        var result = new WxRegisterReturnVo();
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        result.setToken(tokenUtils.generateToken(user));
        result.setUserInfo(userInfo);
        // token
        return ResultUtils.success(result);
    }

    @Override
    public Result<?> login(LoginVo vo, HttpServletRequest request) {
        {
            Set<ConstraintViolation<LoginVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Store[login][block]:param.  request: {}, violation: {}",
                        vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        UserDetails userDetails;
        if (LoginTypeEnum.MOBILE.is(vo.getType())){
            if (StringUtils.isBlank(vo.getCode())){
                return  ResultUtils.message(ResponseCode.AUTH_CAPTCHA_NULL,"验证码为空");
            }
            // 验证码对比
            var code = this.redisUtils.getValue(vo.getPhoneNumber() + "sms").toString();
            if (!vo.getCode().equals(code)){
                return  ResultUtils.message(ResponseCode.AUTH_CAPTCHA_UNMATCH,"验证码不匹配");
            }
            userDetails = userDetailsService.loadUserByUsername(vo.getPhoneNumber());
        }else{
            if (StringUtils.isBlank(vo.getPassword())){
                return  ResultUtils.message(ResponseCode.ACCOUNT_NOT_EXISTS,"密码不存在，请重新输入！");
            }
            userDetails = userDetailsService.loadUserByUsername(vo.getPhoneNumber());
            if (!passwordEncoder.matches(MD5Utils.md5(vo.getPassword()), userDetails.getPassword())) {
                return ResultUtils.message(ResponseCode.ACCOUNT_NOT_EXISTS,"账号或密码错误，请重新输入！");
            }
        }
        if (!userDetails.isEnabled()) {
            return ResultUtils.message(ResponseCode.TOKEN_ILLEGAL,("该账号未启用，请联系管理员！"));
        }
        var user = this.mealUserService.queryByMobile(vo.getPhoneNumber());
        this.LastLogIn(user,request);
        if ( this.mealUserMapper.updateByPrimaryKey(user)<1){
            return  ResultUtils.unknown();
        }
        this.logger.info("登录成功，在security对象中存入{}登陆者信息",vo.getPhoneNumber());
        return ResultUtils.success(new TokenVo().setToken(tokenUtils.generateToken(user)));
    }

    @Override
    public Result<?> sms(String phoneNumber) {
        Random random = new Random();
        int code = 100000 + random.nextInt(899999);
//        smsUtils.sendSms(phoneNumber,"12",code);
        redisUtils.setValueTime(phoneNumber + "sms", code, 5);
        return ResultUtils.success("验证码发送成功！");
    }

    private void LastLogIn(MealUser user,HttpServletRequest request){
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
    }
}
