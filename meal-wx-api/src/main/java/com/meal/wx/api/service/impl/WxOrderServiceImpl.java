package com.meal.wx.api.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.*;
import com.meal.common.mapper.*;
import com.meal.common.model.OrderCartCalamityVo;
import com.meal.common.model.OrderCartVo;
import com.meal.common.transaction.TransactionExecutor;
import com.meal.common.utils.IpUtil;
import com.meal.common.utils.MapperUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxOrderService;
import com.meal.wx.api.util.OrderSnUtils;
import com.meal.wx.api.util.OrderStatusEnum;
import com.meal.wx.api.util.RefundStatusEnum;
import com.meal.wx.api.util.ShipStatusEnum;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WxOrderServiceImpl implements WxOrderService {
    @Resource
    private Validator validator;
    @Resource
    private MealGoodsMapper mealGoodsMapper;

    @Resource
    private MealOrderMapper mealOrderMapper;

    @Resource
    private MealOrderGoodsMapper mealOrderGoodsMapper;


    @Resource
    private MealOrderGoodsCalamityMapper mealOrderGoodsCalamityMapper;

    @Resource
    private MealShopMapper mealShopMapper;

    @Resource
    private MealUserMapper mealUserMapper;

    @Resource
    private MealLittleCalamityMapper mealLittleCalamityMapper;
    @Resource
    private TransactionExecutor transactionExecutor;
    @Resource
    private WxPayService wxPayService;
    private final Logger logger = LoggerFactory.getLogger(WxOrderServiceImpl.class);

    @Override
    public Result<?> submit(WxOrderVo wxOrderVo, Long uid) {
        {
            Set<ConstraintViolation<WxOrderVo>> violations = this.validator.validate(wxOrderVo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Order[WxOrderVo][block]:param.  request: {}, violation: {}", wxOrderVo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(wxOrderVo.getShopId(), Boolean.FALSE);
        if (Objects.isNull(shop)) {
            this.logger.warn("Service-Order[WxOrderVo][block]:shop. uid: {}, request: {}", SecurityUtils.getUserId(), wxOrderVo);
            return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
        }
        LinkedList<Function<Void, Integer>> functions = new LinkedList<>();
        var goodListVo = wxOrderVo.getShoppingCartVos();
        var shopId = wxOrderVo.getShopId();
        List<Long> goodListVoIds = goodListVo.stream().map(OrderCartVo::getGoodsId).collect(Collectors.toList());
        var goodsByShopMap = MapperUtils.goodsMapByShop(mealGoodsMapper, wxOrderVo.getShopId(), goodListVoIds);
        Set<Long> goodsByShopIds = goodsByShopMap.keySet();
        var calamitiesMap = MapperUtils.calamityMapByShopAndGoods(mealLittleCalamityMapper, Collections.emptyList(), new ArrayList<>(goodsByShopIds), shopId);
        List<MealOrderGoods> mealOrderGoodsList = new ArrayList<>();
        List<MealOrderGoodsCalamity> mealOrderCalamityList = new ArrayList<>();
        BigDecimal goodsPrice = BigDecimal.ZERO;
        for (OrderCartVo shoppingCartVo : goodListVo) {
            var good = goodsByShopMap.get(shoppingCartVo.getGoodsId());
            if (Objects.isNull(good) || good.getRetailPrice().compareTo(shoppingCartVo.getPrice()) != 0) {
                return ResultUtils.message(ResponseCode.GOODS_INVALID, ResponseCode.GOODS_INVALID.getMessage());
            }
            mealOrderGoodsList.add(this.createOrderGoods(good, shoppingCartVo));
            goodsPrice = goodsPrice.add(good.getRetailPrice().multiply(BigDecimal.valueOf(shoppingCartVo.getNumber())));
            var calamityVos = shoppingCartVo.getOrderCartCalamityVos();
            if (ObjectUtils.isNotEmpty(calamityVos)) {
                //check 小料的价格
                for (OrderCartCalamityVo calamityVo : calamityVos) {
                    var calamity = calamitiesMap.get(calamityVo.getCalamityId());
                    if (Objects.isNull(calamity) || calamity.getRetailPrice().compareTo(calamityVo.getCalamityPrice()) != 0) {
                        return ResultUtils.message(ResponseCode.CALAMITY_IS_INVALID, ResponseCode.CALAMITY_IS_INVALID.getMessage());
                    }
                    MealOrderGoodsCalamity mealOrderGoodsCalamity = this.createMealOrderGoodsCalamity(calamity, calamityVo);
                    mealOrderGoodsCalamity.setOrderGoodsId((long) goodListVoIds.indexOf(shoppingCartVo.getGoodsId()));
                    mealOrderCalamityList.add(mealOrderGoodsCalamity);
                    goodsPrice = goodsPrice.add(calamity.getRetailPrice().multiply(BigDecimal.valueOf(calamityVo.getCalamityNumber())));
                }
            }
        }
        if (goodsPrice.compareTo(wxOrderVo.getActualPrice()) != 0) {
            return ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL, ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }
        var user = this.mealUserMapper.selectByPrimaryKeyWithLogicalDelete(uid,Boolean.FALSE);
        var mealOrder = this.createOrder(wxOrderVo, user);
        functions.addFirst(nothing -> mealOrderMapper.insertSelective(mealOrder));
        functions.add(nothing -> mealOrderGoodsMapper.batchInsert(mealOrderGoodsList.stream().peek(e -> e.setOrderId(mealOrder.getId())).collect(Collectors.toList())));
        functions.addLast(nothing -> mealOrderGoodsCalamityMapper.batchInsert(mealOrderCalamityList.stream().peek(a -> {
            a.setOrderId(mealOrder.getId());
            a.setOrderGoodsId(mealOrderGoodsList.get(Math.toIntExact(a.getOrderGoodsId())).getId());
        }).collect(Collectors.toList())));
        if (this.transactionExecutor.transaction(functions,1+mealOrderGoodsList.size()+mealOrderCalamityList.size())) {
            return ResultUtils.success();
        }
        return ResultUtils.unknown();
    }



    private MealOrder createOrder(WxOrderVo wxOrderVo, MealUser user) {
        // 通过MyBatis mapper查询对应的用户对象
        // 如果查询到的用户对象不为null，则创建一个新的订单对象，并设置订单属性
        return Optional.ofNullable(user).map(u -> {
                    MealOrder mealOrder = new MealOrder();
                    mealOrder.setUserId(user.getId()); // 用户ID
                    mealOrder.setShopId(wxOrderVo.getShopId()); // 商铺ID
                    mealOrder.setOrderSn(OrderSnUtils.generateOrderSn("meal")); // 订单号
                    mealOrder.setOrderStatus(OrderStatusEnum.UNPAID.getMapping()); // 订单状态：待支付
                    mealOrder.setShipStatus(ShipStatusEnum.NOT_SHIP.getMapping()); // 发货状态：未发货
                    mealOrder.setRefundStatus(RefundStatusEnum.CAN_APPLY.getMapping()); // 退款状态：可以申请
                    mealOrder.setConsignee(u.getNickname()); // 收货人
                    mealOrder.setMobile(u.getMobile()); // 收货人电话
                    mealOrder.setAddress(""); // 收货地址
                    mealOrder.setMessage(wxOrderVo.getMessage()); // 订单留言
                    mealOrder.setGoodsPrice(wxOrderVo.getOrderPrice()); // 商品总价
                    mealOrder.setFreightPrice(BigDecimal.ZERO); // 运费
                    mealOrder.setCouponPrice(BigDecimal.ZERO); // 优惠券抵扣金额
                    mealOrder.setOrderPrice(wxOrderVo.getOrderPrice()); // 订单总价
                    mealOrder.setActualPrice(wxOrderVo.getActualPrice()); // 实际支付金额
                    mealOrder.setPayId(null); // 支付ID
                    mealOrder.setPayTime(null); // 支付时间
                    mealOrder.setShipSn(OrderSnUtils.generateOrderSn("mealShip")); // 发货单号
                    mealOrder.setShipTime(null); // 发货时间
                    mealOrder.setRefundAmount(null); // 退款金额
                    mealOrder.setRefundContent(null); // 退款原因
                    mealOrder.setRefundTime(null); // 退款时间
                    mealOrder.setConfirmTime(null); // 确认收货时间
                    mealOrder.setEndTime(null); // 订单完成时间
                    mealOrder.setAddTime(LocalDateTime.now()); // 订单添加时间
                    mealOrder.setUpdateTime(LocalDateTime.now()); // 订单更新时间
                    return mealOrder;
                })
                // 如果查询到的用户对象为null，则抛出IllegalArgumentException异常
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    }

    private MealOrderGoods createOrderGoods(MealGoods good, OrderCartVo shoppingCartVo) {
        return Optional.ofNullable(good).map(g -> {
                    MealOrderGoods mealOrderGoods = new MealOrderGoods();
                    mealOrderGoods.setGoodsId(g.getId()); // 设置MealOrderGoods的goodsId字段为MealGoods的id
                    mealOrderGoods.setGoodsName(g.getName()); // 设置MealOrderGoods的goodsName字段为MealGoods的name
                    mealOrderGoods.setGoodsSn(g.getGoodsSn());// 设置MealOrderGoods的goodsSn字段为MealGoods的goodsSn
                    mealOrderGoods.setNumber(shoppingCartVo.getNumber()); // 设置MealOrderGoods的number字段为OrderCartVo的number
                    mealOrderGoods.setPrice(g.getPrice()); // 设置MealOrderGoods的price字段为MealGoods的price
                    mealOrderGoods.setPicUrl(g.getPicUrl());
                    return mealOrderGoods;
                }) // 设置MealOrderGoods的picUrl字段为MealGoods的picUrl
                .orElseThrow(() -> new IllegalArgumentException("Invalid meal goods"));
    }

    private MealOrderGoodsCalamity createMealOrderGoodsCalamity(MealLittleCalamity calamity, OrderCartCalamityVo orderCartCalamityVo) {
        return Optional.ofNullable(calamity).map(c -> {
            MealOrderGoodsCalamity mealOrderCalamity = new MealOrderGoodsCalamity();
            mealOrderCalamity.setCalamityId(calamity.getId());
            mealOrderCalamity.setCalamitySn(calamity.getUnit());
            mealOrderCalamity.setCalamityName(calamity.getName());
            mealOrderCalamity.setPrice(orderCartCalamityVo.getCalamityPrice());
            mealOrderCalamity.setNumber(orderCartCalamityVo.getCalamityNumber());
            return mealOrderCalamity;
        }).orElseThrow(() -> new IllegalArgumentException("Invalid meal goods calamity"));
    }

    @Override
    public Result<?> prepay(Long uid, Long orderId) {
        if (Objects.isNull(orderId)){
            return  ResultUtils.message(ResponseCode.PARAMETER_ERROR,ResponseCode.PARAMETER_ERROR.getMessage());
        }
        var example = new MealOrderExample();
        example.createCriteria().andUserIdEqualTo(uid).andDeletedEqualTo(Boolean.FALSE).andIdEqualTo(orderId);
        var order = this.mealOrderMapper.selectOneByExample(example);
        if (Objects.isNull(order)){
            return  ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL,ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }
        if (OrderStatusEnum.UNPAID.not(order.getOrderStatus())){
            return  ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL,ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }
        var user = this.mealUserMapper.selectByPrimaryKeyWithLogicalDelete(uid,Boolean.FALSE);
        String wxOpenid = user.getWxOpenid();
        if (Objects.isNull(wxOpenid)){
            return  ResultUtils.message(ResponseCode.AUTH_OPENID_UNACCESS,ResponseCode.AUTH_OPENID_UNACCESS.getMessage());
        }
        WxPayMpOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(order.getOrderSn());
            orderRequest.setOpenid(wxOpenid);
            orderRequest.setBody("订单：" + order.getOrderSn());
            // 元转成分
            int fee = 0;
            BigDecimal actualPrice = order.getActualPrice();
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(user.getLastLoginIp());
            result = wxPayService.createOrder(orderRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.message(ResponseCode.ORDER_PAY_FAIL, ResponseCode.ORDER_PAY_FAIL.getMessage());
        }
        return ResultUtils.success(result);
    }

    @Override
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        String xmlResult = null;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        WxPayOrderNotifyResult result = null;
        try {
            result = wxPayService.parseOrderNotifyResult(xmlResult);

            if(!WxPayConstants.ResultCode.SUCCESS.equals(result.getResultCode())){
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
            if(!WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())){
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        this.logger.info("处理腾讯支付平台的订单支付");
        this.logger.info("结果:{}",result);
        String orderSn = result.getOutTradeNo();
        String payId = result.getTransactionId();

        // 分转化成元
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        MealOrder mealOrder = this.selectOrderBySn(orderSn);

        return null;
    }

    private  MealOrder selectOrderBySn(String orderSn) {
        var example = new MealOrderExample();
        example.createCriteria().andOrderSnEqualTo(orderSn).andDeletedEqualTo(Boolean.FALSE);
        return  this.mealOrderMapper.selectOneByExample(example);
    }
}







