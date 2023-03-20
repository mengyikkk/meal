package com.meal.wx.api.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.config.MealProperties;
import com.meal.common.dto.*;
import com.meal.common.enums.LoginTypeEnum;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.mapper.MealUserShopMapper;
import com.meal.common.model.WxLoginVo;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.*;
import com.meal.common.model.LoginVo;
import com.meal.common.model.WxRegisterReturnVo;
import com.meal.common.model.WxRegisterVo;
import com.meal.wx.api.service.WxAuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private WxMaService wxService;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private MealUserService mealUserService;

    //    @Resource
//    private NotifyService notifyService;
    @Resource
    private Validator validator;

    @Resource
    private TokenUtils tokenUtils;
    @Resource
    private MealUserMapper mealUserMapper;
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private MealProperties mealProperties;

    @Resource
    private MealShopMapper mealShopMapper;

    @Resource
    private MealUserShopMapper mealUserShopMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Result<WxRegisterReturnVo> register(WxRegisterVo vo, HttpServletRequest request) {
        {
            Set<ConstraintViolation<WxRegisterVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Store[register][block]:param.  request: {}, violation: {}", vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var mobile = vo.getMobile();
        var password = vo.getPassword();

        // 如果是小程序注册，则必须非空
        // 其他情况，可以为空
        String wxCode = vo.getWxCode();
        var user = new MealUser();
        if (Objects.nonNull(mealUserService.queryByMobile(mobile))) {
            return ResultUtils.code(ResponseCode.AUTH_MOBILE_REGISTERED);
        }
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
            var checkUser = this.mealUserService.queryByUserName(openId);
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
        this.LastLogIn(user, request);
        var example = new MealUserExample();
        example.createCriteria().andWxOpenidEqualTo(openId);
        var oldUser = this.mealUserMapper.selectOneByExample(example);
        if (Objects.nonNull(oldUser)) {
            if (this.mealUserMapper.updateByExampleSelective(user, example) < 1) {
                return ResultUtils.code(ResponseCode.TIME_OUT);
            }
        } else {
            if (this.mealUserMapper.insertSelective(user) < 1) {
                return ResultUtils.code(ResponseCode.TIME_OUT);
            }
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
    public Result<?> loginByWx(WxLoginVo info, HttpServletRequest request) {
        {
            Set<ConstraintViolation<WxLoginVo>> violations = this.validator.validate(info);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Store[register][block]:param.  request: {}, violation: {}", info, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        String phone;
        String openId;
        String sessionKey;
        var wxUserService =this.wxService.getUserService();
        var userWx = new WxMaUserInfo();
        try {
            WxMaJscode2SessionResult result = wxUserService.getSessionInfo(info.getCode());
            openId = result.getOpenid();
            this.logger.info("获取到用户openId:{}", openId);
            sessionKey = result.getSessionKey();
            WxMaPhoneNumberInfo phoneInfo = wxUserService.getPhoneNoInfo(info.getCodePhone());
            phone = phoneInfo.getPurePhoneNumber();
            this.logger.info("获取到用户Phone:{}", phone);
            userWx =wxUserService.getUserInfo(sessionKey,info.getEncryptedData(),info.getIv());
            this.logger.info("获取到用户Phone:{}", JsonUtils.toJsonKeepNullValue(userWx));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.code(ResponseCode.AUTH_OPENID_UNACCESS);
        }
        if (Objects.isNull(phone)){
            return ResultUtils.message(ResponseCode.AUTH_INVALID_MOBILE,"无效手机号");
        }
        var example = new MealUserExample();
        example.createCriteria().andLogicalDeleted(Boolean.TRUE).andWxOpenidEqualTo(openId);
        var user = this.mealUserMapper.selectOneByExample(example);
        if (Objects.nonNull(user)) {    //已经注册过的用户处理
            this.logger.info("用户:{}已经注册过走更新流程",user.getUsername());
            if (!user.isEnabled()) {
                return ResultUtils.message(ResponseCode.TOKEN_ILLEGAL, ("该账号未启用，请联系管理员！"));
            }
            user.setMobile(phone);
            user.setUsername(openId);
            this.coverUserByWx(userWx,user);
            this.LastLogIn(user, request);
            if (this.mealUserMapper.updateByPrimaryKey(user) < 1) {
                return ResultUtils.unknown();
            }
        } else {
            user = new MealUser();
            user.setUsername(openId);
            user.setMobile(phone);
            user.setSessionKey(sessionKey);
            user.setWxOpenid(openId);
            this.coverUserByWx(userWx,user);
            this.LastLogIn(user, request);
            if (this.mealUserMapper.insertSelective(user) < 1) {
                return ResultUtils.unknown();
            }
        }
        var result = new WxRegisterReturnVo();
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        result.setToken(tokenUtils.generateToken(user));
        result.setUserInfo(userInfo);
        this.logger.info("登录成功，在security对象中存入{}登陆者信息", phone);
        return ResultUtils.success(result);
    }

    @Override
    public Result<?> login(LoginVo vo, HttpServletRequest request) {
        {
            Set<ConstraintViolation<LoginVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Store[login][block]:param.  request: {}, violation: {}", vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        UserDetails userDetails;
        if (LoginTypeEnum.MOBILE.is(vo.getType())) {
            if (StringUtils.isBlank(vo.getCode())) {
                return ResultUtils.message(ResponseCode.AUTH_CAPTCHA_NULL, "验证码为空");
            }
            // 验证码对比
            var code = this.redisUtils.getValue(vo.getPhoneNumber() + "sms").toString();
            if (!vo.getCode().equals(code)) {
                return ResultUtils.message(ResponseCode.AUTH_CAPTCHA_UNMATCH, "验证码不匹配");
            }
            userDetails = userDetailsService.loadUserByUsername(vo.getPhoneNumber());
        } else {
            if (StringUtils.isBlank(vo.getPassword())) {
                return ResultUtils.message(ResponseCode.ACCOUNT_NOT_EXISTS, "密码不存在，请重新输入！");
            }
            userDetails = userDetailsService.loadUserByUsername(vo.getPhoneNumber());
            if (!passwordEncoder.matches(MD5Utils.md5(vo.getPassword()), userDetails.getPassword())) {
                return ResultUtils.message(ResponseCode.ACCOUNT_NOT_EXISTS, "账号或密码错误，请重新输入！");
            }
        }
        if (!userDetails.isEnabled()) {
            return ResultUtils.message(ResponseCode.TOKEN_ILLEGAL, ("该账号未启用，请联系管理员！"));
        }
        var user = this.mealUserService.queryByMobile(vo.getPhoneNumber());
        this.LastLogIn(user, request);
        if (this.mealUserMapper.updateByPrimaryKey(user) < 1) {
            return ResultUtils.unknown();
        }
        this.logger.info("登录成功，在security对象中存入{}登陆者信息", vo.getPhoneNumber());
        return ResultUtils.success(tokenUtils.generateToken(user));
    }

    @Override
    public Result<?> sms(String phoneNumber) {
        Random random = new Random();
        int code = 100000 + random.nextInt(899999);
        SmsUtils.sendSms(phoneNumber, "12", code, mealProperties.getSms());
        redisUtils.setValueTime(phoneNumber + "sms", code, 10);
        return ResultUtils.success("验证码发送成功！");
    }

    @Override
    public Result<?> refresh(String token) {
        //2. 判断token是否存在
        if (org.springframework.util.StringUtils.hasText(token)) {
            //拿到token主体
            if (token.startsWith(tokenHead)) token = token.substring(tokenHead.length());
        } else {
            return ResultUtils.message(ResponseCode.PARAMETER_ERROR, "无效参数");
        }
        if (!tokenUtils.isExpiration(token)) {
            return ResultUtils.success(tokenUtils.refreshToken(token));
        }
        return ResultUtils.message(ResponseCode.TOKEN_ILLEGAL, "登录过期，请重新登录！");
    }

    @Override
    public Result<?> bind(Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-bind[list][block]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var userId = SecurityUtils.getUserId();
        var example = new MealUserShopExample();
        example.createCriteria().andLogicalDeleted(Boolean.TRUE).andUserIdEqualTo(userId);
        var model = this.mealUserShopMapper.selectOneByExample(example);
        if (Objects.nonNull(model)) {
            model.setShopId(shopId);
            if (this.mealUserShopMapper.updateByPrimaryKeySelective(model) < 1) {
                return ResultUtils.code(ResponseCode.UNKNOWN);
            }
        } else {
            model = new MealUserShop();
            model.setUserId(userId);
            model.setShopId(shopId);
            if (this.mealUserShopMapper.insertSelective(model) < 1) {
                return ResultUtils.code(ResponseCode.UNKNOWN);
            }
        }
        return ResultUtils.success(shopId);
    }

    private void LastLogIn(MealUser user, HttpServletRequest request) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
    }
    private void coverUserByWx(WxMaUserInfo userInfo,MealUser user){
        user.setNickname(userInfo.getNickName());
        user.setAvatar(userInfo.getAvatarUrl());
    }
}
