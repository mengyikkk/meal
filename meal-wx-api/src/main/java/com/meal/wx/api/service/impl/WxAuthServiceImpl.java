package com.meal.wx.api.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.config.MealProperties;
import com.meal.common.dto.*;
import com.meal.common.enums.LoginTypeEnum;
import com.meal.common.mapper.*;
import com.meal.common.model.*;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.*;
import com.meal.wx.api.dto.WxSendMessageVo;
import com.meal.wx.api.service.WxAuthService;
import com.meal.wx.api.util.OrderStatusEnum;
import com.meal.wx.api.util.WxTemplateSender;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WxAuthServiceImpl implements WxAuthService {

    private final Logger logger = LoggerFactory.getLogger(WxAuthServiceImpl.class);
    @Resource
    private WxMaService wxService;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private MealUserService mealUserService;
    @Resource
    private WebClient webClient;
    @Resource
    private MealOrderMapper mealOrderMapper;

    @Resource
    private MealOrderGoodsMapper mealOrderGoodsMapper;
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

    @Resource
    private WxTemplateSender wxTemplateSender;

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
        example.createCriteria().andUsernameEqualTo(user.getUsername());
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
        result.setShopId(this.getShopByBind(user.getId()));
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
        String openId;
        String sessionKey;
        var wxUserService = this.wxService.getUserService();
        try {
            WxMaJscode2SessionResult result = wxUserService.getSessionInfo(info.getCode());
            openId = result.getOpenid();
            sessionKey = result.getSessionKey();
            this.logger.info("获取到用户openId:{}", openId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.code(ResponseCode.AUTH_OPENID_UNACCESS);
        }
        var example = new MealUserExample();
        example.createCriteria().andLogicalDeleted(Boolean.TRUE).andWxOpenidEqualTo(openId);
        var user = this.mealUserMapper.selectOneByExample(example);
        if (Objects.nonNull(user)) {    //已经注册过的用户处理
            this.logger.info("用户:{}已经注册过走更新流程", user.getUsername());
            if (!user.isEnabled()) {
                return ResultUtils.message(ResponseCode.TOKEN_ILLEGAL, ("该账号未启用，请联系管理员！"));
            }
            this.LastLogIn(user, request);
            if (this.mealUserMapper.updateByPrimaryKey(user) < 1) {
                return ResultUtils.unknown();
            }
        } else {
            user = new MealUser();
            user.setUsername(openId);
            user.setSessionKey(sessionKey);
            user.setWxOpenid(openId);
            this.LastLogIn(user, request);
            if (this.mealUserMapper.insertSelective(user) < 1) {
                return ResultUtils.unknown();
            }
        }
        var result = new WxRegisterReturnVo();
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        userInfo.setGender(user.getGender());
        userInfo.setMobile(user.getMobile());
        result.setToken(tokenUtils.generateToken(user));
        result.setUserInfo(userInfo);
        result.setShopId(this.getShopByBind(user.getId()));
        this.logger.info("登录成功，在security对象中存入{}登陆者信息", openId);
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
        var result = new WxRegisterReturnVo();
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        userInfo.setBirthday(user.getBirthday());
        userInfo.setGender(user.getGender());
        userInfo.setMobile(user.getMobile());
        result.setToken(tokenUtils.generateToken(user));
        result.setUserInfo(userInfo);
        result.setShopId(this.getShopByBind(user.getId()));
        this.logger.info("登录成功，在security对象中存入{}登陆者信息", vo.getPhoneNumber());
        return ResultUtils.success(result);
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

    @Override
    public Result<?> getShopId(Long userId) {
        return ResultUtils.success(getShopByBind(userId));
    }

    @Override
    public Result<?> update(UserDetailsVo vo, Long userId) {
        var user = mealUserMapper.selectByPrimaryKey(userId);
        Optional.ofNullable(vo.getBirthday()).ifPresent(user::setBirthday);
        Optional.ofNullable(vo.getGender()).ifPresent(user::setGender);
        Optional.ofNullable(vo.getNickName()).ifPresent(user::setNickname);
        if (mealUserMapper.updateByPrimaryKeySelective(user) < 1) {
            return ResultUtils.unknown();
        }
        return ResultUtils.success();
    }

    @Override
    public Result<?> info(Long userId) {
        MealUser mealUser = this.mealUserMapper.selectByPrimaryKey(userId);
        return ResultUtils.success(new UserInfo().setGender(mealUser.getGender()).setMobile(mealUser.getMobile()).setNickName(mealUser.getNickname()).setAvatarUrl(mealUser.getAvatar()).setBirthday(mealUser.getBirthday()));
    }


    @Override
    public Result<?> send(LocalDateTime shipTime, Integer isTimeOnSale) {
        String id = "z3WB96pX2ASunRRQRcaBhwioVCiKrjCDritU6VW0yQ4";
        var orderExample = new MealOrderExample();
        orderExample.createCriteria().andShipTimeLessThan(shipTime.plusDays(1)).andShipTimeGreaterThanOrEqualTo(shipTime)
                .andIsTimeOnSaleEqualTo(isTimeOnSale).andOrderStatusEqualTo(OrderStatusEnum.PAID.getMapping());
        List<MealOrder> mealOrders = this.mealOrderMapper.selectByExample(orderExample);
        if (CollectionUtils.isEmpty(mealOrders)) {
            return ResultUtils.success();
        }
        var userIds = mealOrders.stream().map(MealOrder::getUserId).collect(Collectors.toList());
        var example = new MealUserExample();
        example.createCriteria().andIdIn(userIds);
        var openIdMap = this.mealUserMapper.selectByExample(example).stream().collect(Collectors.toMap(MealUser::getId, MealUser::getWxOpenid));
        var shopIds = mealOrders.stream().map(MealOrder::getShopId).collect(Collectors.toList());
        var shopExample = new MealShopExample();
        shopExample.createCriteria().andIdIn(shopIds);
        var shopMap = this.mealShopMapper.selectByExample(shopExample).stream().collect(Collectors.toMap(MealShop::getId,
                MealShop::getName));
        mealOrders.parallelStream().forEach(
                e -> {
                    WxSendMessageVo wxSendMessageVo = new WxSendMessageVo();
                    Map<String, Object> data = Stream.of(
                                    new AbstractMap.SimpleEntry<>("thing23", shopMap.get(e.getShopId())),
                                    new AbstractMap.SimpleEntry<>("character_string19", e.getShipSn()),
                                    new AbstractMap.SimpleEntry<>("thing20", "您的餐点已为您准备好 请前往取餐区取餐。"),
                                    new AbstractMap.SimpleEntry<>("phone_number32", "15988888888"))
                            .collect(Collectors.toMap(Map.Entry::getKey, a->a.getValue() == null ? null :mapValueToMap(a.getValue())));
                    wxSendMessageVo.setData(data);
                    wxSendMessageVo.setTemplate_id(id);
                    wxSendMessageVo.setTouser(openIdMap.get(e.getUserId()));
                    if (wxTemplateSender.sendMessage(wxSendMessageVo)) {
                        e.setOrderStatus(OrderStatusEnum.COMPLETED.getMapping());
                        this.mealOrderMapper.updateByPrimaryKeySelective(e);
                    }
                }
        );
        return ResultUtils.success();
    }


    private Map<String, String> mapValueToMap(Object value) {
        Map<String, String> map = new HashMap<>();
        map.put("value", value.toString());
        return map;
    }

    private Long getShopByBind(Long userId) {
        var example = new MealUserShopExample();
        example.createCriteria().andUserIdEqualTo(userId);
        MealUserShop mealUserShop = this.mealUserShopMapper.selectOneByExample(example);
        if (Objects.isNull(mealUserShop)) {
            return 6L;
        }
        return mealUserShop.getShopId();
    }

    private void LastLogIn(MealUser user, HttpServletRequest request) {
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
    }

    private void coverUserByWx(WxMaUserInfo userInfo, MealUser user) {
        user.setNickname(userInfo.getNickName());
        user.setAvatar(userInfo.getAvatarUrl());
    }

    public Result<?> bindPhone(Long userId, String code) {
        String accessToken = wxTemplateSender.getAccessToken();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://api.weixin.qq.com/wxa/business/getuserphonenumber")
                .queryParam("access_token", accessToken);
        HashMap<String, Object> requestParam = new HashMap<>();
        requestParam.put("code", code);
        String jsonStr = JsonUtils.toJson(requestParam);
        return this.webClient.post()
                .uri(uriBuilder.build().toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonStr)
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class)
                                .map(responseBody -> {
                                    var map = JsonUtils.toGeneric(responseBody,new TypeReference<Map<String, Object>>() {});
                                    String errcode = map.get("errcode").toString();
                                    if("0".equals(errcode)){
                                        String phoneNumber = JsonUtils.toMap(map.get("phone_info")).get("phoneNumber").toString();
                                        MealUser mealUser = new MealUser();
                                        mealUser.setId(userId);
                                        mealUser.setMobile(phoneNumber);
                                        if (this.mealUserMapper.updateByPrimaryKeySelective(mealUser)<1){
                                            return ResultUtils.unknown();
                                        }
                                        return ResultUtils.success();
                                    } else if ("-1".equals(errcode)){
                                        logger.error("userID:{}微信小程序获取手机号错误：{}", userId,map.get("errmsg"));
                                        return ResultUtils.unknown();
                                    }else if ("40029".equals(errcode)){
                                        logger.error("userID:{}微信小程序获取手机号code无效：{}", userId,map.get("errmsg"));
                                        return ResultUtils.unknown();
                                    }
                                    return ResultUtils.success();
                                });
                    } else {
                        logger.error("Failed to get phone number, status code: {}", response.statusCode());
                        return Mono.just(ResultUtils.unknown());
                    }
                })
                .onErrorResume(e -> {
                    logger.error("微信小程序获取AccessToken异常，Exception：{}", e.getMessage());
                    return Mono.just(ResultUtils.unknown());
                })
                .block();
    }

}
