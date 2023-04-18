package com.meal.wx.api.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.CombineTransactionsRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.*;
import com.meal.common.mapper.*;
import com.meal.common.model.*;
import com.meal.common.transaction.TransactionExecutor;
import com.meal.common.utils.JsonUtils;
import com.meal.common.utils.MapperUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.config.WxProperties;
import com.meal.wx.api.service.WxCartService;
import com.meal.wx.api.service.WxOrderService;
import com.meal.wx.api.util.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WxOrderServiceImpl implements WxOrderService {
    @Resource
    private Validator validator;
    @Resource
    private MealGoodsMapper mealGoodsMapper;

    @Resource
    private WxProperties wxProperties;

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
    @Resource
    private MealOrderWxMapper mealOrderWxMapper;
    @Resource
    private WxCartService wxCartService;
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
        var user = this.mealUserMapper.selectByPrimaryKeyWithLogicalDelete(uid, Boolean.FALSE);
        if (Objects.isNull(user.getMobile())) {
            return ResultUtils.message(ResponseCode.AUTH_NOT_MOBILE, ResponseCode.AUTH_NOT_MOBILE.getMessage());
        }
        List<WxOrderSonVo> orders = wxOrderVo.getOrders();
        int size = orders.size();
        var shopId = wxOrderVo.getShopId();
        // 查找购物车中的商品信息
        LinkedList<Function<Void, Integer>> functions = new LinkedList<>();
        //找到所有订单的商品集合
        List<MealGoods> goodsByAllOrder = new ArrayList<>();
        List<Long> goodIds = orders.stream().flatMap(e -> e.getGoods().stream()).map(OrderCartVo::getGoodsId).collect(Collectors.toList());
        var mapByIsTimeOnSale = MapperUtils.checkGoodsByIsTimeOnSale(mealGoodsMapper, shopId, goodIds);
        //check 提供订单的商品是否属于某个时间段
        for (WxOrderSonVo order : orders) {
            List<MealGoods> mealGoods = mapByIsTimeOnSale.get(order.getIsTimeOnSale());
            Set<Long> goods = mealGoods.stream().map(MealGoods::getId).collect(Collectors.toSet());
            List<Long> orderGoodIds = order.getGoods().stream().map(OrderCartVo::getGoodsId).collect(Collectors.toList());
            if (goods.containsAll(orderGoodIds)) {
                goodsByAllOrder.addAll(mealGoods);
            } else {
                orderGoodIds.removeAll(goods);
                this.logger.info("Service-Order[WxOrderVo][block]:shop. ids: {} 不属于isTimeONSale:{}", orderGoodIds, order.getIsTimeOnSale());
                return ResultUtils.message(ResponseCode.ORDER_GOODSISTIME_CHECKOUT_FAIL, "商品ID:{" + orderGoodIds + "}" + ResponseCode.ORDER_GOODSISTIME_CHECKOUT_FAIL.getMessage());
            }
        }
        //取出订单中所有商品的信息
        List<Long> goodListVoIds = goodsByAllOrder.stream().map(MealGoods::getId).collect(Collectors.toList());
        var goodsByShopMap = goodsByAllOrder.stream().collect(Collectors.toMap(MealGoods::getId, Function.identity()));
        var calamitiesMap = MapperUtils.calamityMapByShopAndGoods(mealLittleCalamityMapper, Collections.emptyList(), goodListVoIds, shopId);
        List<MealOrderGoods> mealOrderGoodsList = new ArrayList<>();
        List<MealOrder> mealOrders = new ArrayList<>();
        List<MealOrderGoodsCalamity> mealOrderCalamityList = new ArrayList<>();
        BigDecimal orderPrice = BigDecimal.ZERO;
        // 遍历购物车中的商品
        var orderSn = OrderSnUtils.generateOrderSn("meal", mealOrderMapper);
        for (WxOrderSonVo order : orders) {
            BigDecimal goodsPrice = BigDecimal.ZERO;
            for (OrderCartVo shoppingCartVo : order.getGoods()) {
                // 检查购物车中的商品是否有效
                var good = goodsByShopMap.get(shoppingCartVo.getGoodsId());
                if (Objects.isNull(good)) {
                    return ResultUtils.message(ResponseCode.GOODS_INVALID, ResponseCode.GOODS_INVALID.getMessage());
                }
                // 将商品信息加入订单商品列表中，并累加订单价格
                mealOrderGoodsList.add(this.createOrderGoods(good, shoppingCartVo, (long) orders.indexOf(order)));
                goodsPrice = goodsPrice.add(good.getRetailPrice().multiply(BigDecimal.valueOf(shoppingCartVo.getNumber())));
                // 检查购物车中的小料是否有效
                var calamityVos = shoppingCartVo.getCalamityVos();
                if (ObjectUtils.isNotEmpty(calamityVos)) {
                    //check 小料的价格
                    for (OrderCartCalamityVo calamityVo : calamityVos) {
                        var calamity = calamitiesMap.get(calamityVo.getCalamityId());
                        if (Objects.isNull(calamity)) {
                            return ResultUtils.message(ResponseCode.CALAMITY_IS_INVALID, ResponseCode.CALAMITY_IS_INVALID.getMessage());
                        }
                        MealOrderGoodsCalamity mealOrderGoodsCalamity = this.createMealOrderGoodsCalamity(calamity, calamityVo);
                        mealOrderGoodsCalamity.setOrderGoodsId((long) goodListVoIds.indexOf(shoppingCartVo.getGoodsId()));
                        mealOrderCalamityList.add(mealOrderGoodsCalamity);
                        goodsPrice = goodsPrice.add(calamity.getRetailPrice().multiply(BigDecimal.valueOf(calamityVo.getCalamityNumber())));
                    }
                }
            }
            var mealOrder = this.createOrder(order, user, wxOrderVo.getShopId(), wxOrderVo.getMessage(), orderSn, goodsPrice);
            mealOrders.add(mealOrder);
            orderPrice = orderPrice.add(goodsPrice);
        }
        if (orderPrice.compareTo(wxOrderVo.getActualPrice()) != 0) {
            return ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL, ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }
        functions.addFirst(nothing -> mealOrderMapper.batchInsert(mealOrders));
        functions.add(nothing -> mealOrderGoodsMapper.batchInsert(mealOrderGoodsList.stream().peek(e -> e.setOrderId(mealOrders.get(Math.toIntExact(e.getOrderId())).getId())).collect(Collectors.toList())));
        if (!mealOrderCalamityList.isEmpty()) {
            functions.addLast(nothing -> mealOrderGoodsCalamityMapper.batchInsert(mealOrderCalamityList.stream().peek(a -> {
                a.setOrderId(mealOrderGoodsList.get(Math.toIntExact(a.getOrderGoodsId())).getOrderId());
                a.setOrderGoodsId(mealOrderGoodsList.get(Math.toIntExact(a.getOrderGoodsId())).getId());
            }).collect(Collectors.toList())));
        }
        size += mealOrderGoodsList.size();
        size += mealOrderCalamityList.size();
        if (this.transactionExecutor.transaction(functions, size)) {
            //清空购物车
            this.wxCartService.deleteShoppingCart(uid, shopId);
            return ResultUtils.success(orderSn);
        }
        return ResultUtils.unknown();
    }


    private MealOrder createOrder(WxOrderSonVo wxOrderVo, MealUser user, Long shopId, String message, String orderSn, BigDecimal goodsPrice) {
        // 通过MyBatis mapper查询对应的用户对象
        // 如果查询到的用户对象不为null，则创建一个新的订单对象，并设置订单属性
        return Optional.ofNullable(user).map(u -> {
                    MealOrder mealOrder = new MealOrder();
                    mealOrder.setIsTimeOnSale(wxOrderVo.getIsTimeOnSale());
                    mealOrder.setUserId(user.getId()); // 用户ID
                    mealOrder.setShopId(shopId); // 商铺ID
                    //设置 发货日期为明天
                    mealOrder.setShipTime(LocalDateTime.now().plusDays(1));
                    mealOrder.setOrderSn(orderSn); // 订单号
                    mealOrder.setOrderStatus(OrderStatusEnum.UNPAID.getMapping()); // 订单状态：待支付
                    mealOrder.setShipStatus(ShipStatusEnum.NOT_SHIP.getMapping()); // 发货状态：未发货
                    mealOrder.setRefundStatus(RefundStatusEnum.CAN_APPLY.getMapping()); // 退款状态：可以申请
                    mealOrder.setConsignee(u.getNickname()); // 收货人
                    mealOrder.setMobile(u.getMobile()); // 收货人电话
                    mealOrder.setAddress(""); // 收货地址
                    mealOrder.setMessage(message); // 订单留言
                    mealOrder.setGoodsPrice(goodsPrice); // 商品总价
                    mealOrder.setFreightPrice(BigDecimal.ZERO); // 运费
                    mealOrder.setCouponPrice(BigDecimal.ZERO); // 优惠券抵扣金额
                    mealOrder.setOrderPrice(goodsPrice); // 订单总价
                    mealOrder.setActualPrice(goodsPrice); // 实际支付金额
                    return mealOrder;
                })
                // 如果查询到的用户对象为null，则抛出IllegalArgumentException异常
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    }


    private MealOrderGoods createOrderGoods(MealGoods good, OrderCartVo shoppingCartVo, Long orderIndex) {
        return Optional.ofNullable(good).map(g -> {
                    MealOrderGoods mealOrderGoods = new MealOrderGoods();
                    mealOrderGoods.setGoodsId(g.getId()); // 设置MealOrderGoods的goodsId字段为MealGoods的id
                    mealOrderGoods.setOrderId(orderIndex);
                    mealOrderGoods.setGoodsName(g.getName()); // 设置MealOrderGoods的goodsName字段为MealGoods的name
                    mealOrderGoods.setGoodsSn(g.getGoodsSn());// 设置MealOrderGoods的goodsSn字段为MealGoods的goodsSn
                    mealOrderGoods.setUnit(g.getUnit());
                    mealOrderGoods.setNumber(shoppingCartVo.getNumber()); // 设置MealOrderGoods的number字段为OrderCartVo的number
                    mealOrderGoods.setPrice(g.getRetailPrice()); // 设置MealOrderGoods的price字段为MealGoods的price
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
            mealOrderCalamity.setUnit(c.getUnit());
            mealOrderCalamity.setPrice(calamity.getRetailPrice());
            mealOrderCalamity.setNumber(orderCartCalamityVo.getCalamityNumber());
            return mealOrderCalamity;
        }).orElseThrow(() -> new IllegalArgumentException("Invalid meal goods calamity"));
    }

    @Override
    public Result<?> prepay(Long uid, String orderSn,String message) {
        if (Objects.isNull(orderSn)) {
            return ResultUtils.message(ResponseCode.PARAMETER_ERROR, ResponseCode.PARAMETER_ERROR.getMessage());
        }
        var orders = this.getValidOrder(uid, orderSn);
        if (ObjectUtils.isEmpty(orders)) {
            return ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL, ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }
        if (OrderStatusEnum.UNPAID.not(orders.get(0).getOrderStatus())) {
            return ResultUtils.message(ResponseCode.ORDER_STATUS_FAIL, ResponseCode.ORDER_STATUS_FAIL.getMessage());
        }
        var user = this.mealUserMapper.selectByPrimaryKeyWithLogicalDelete(uid, Boolean.FALSE);
        return this.prepaySon(user, orderSn, orders.stream().map(MealOrder::getActualPrice).reduce(BigDecimal.ZERO,
                BigDecimal::add),message);
    }

    @Override
    public Result<?> refund(Long uid, String orderSn) {
        // 判断orderId是否为空
        if (Objects.isNull(orderSn)) {
            return ResultUtils.message(ResponseCode.PARAMETER_ERROR, ResponseCode.PARAMETER_ERROR.getMessage());
        }
        // 根据userId、orderId查找订单
        var orders = getValidOrder(uid, orderSn);
        // 如果订单不存在，返回错误信息
        if (ObjectUtils.isEmpty(orders)) {
            return ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL, ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }
        MealOrder order = orders.get(0);
        MealOrder orderNew = new MealOrder();
        // 根据userId查找用户信息
        if (!OrderStatusEnum.isCanceled(order)) {
            return ResultUtils.message(ResponseCode.ORDER_CONFIRM_NOT_ALLOWED, ResponseCode.ORDER_CONFIRM_NOT_ALLOWED.getMessage());
        }
        // 设置订单为已退款状态，设置相关时间和金额信息
        orderNew.setOrderStatus(OrderStatusEnum.REFUNDED.getMapping());
        orderNew.setOrderSn(orderSn);
        orderNew.setRefundTime(LocalDateTime.now());
        // 更新订单信息
        if (this.mealOrderMapper.updateByOrderSn(orderNew) < orders.size()) {
            return ResultUtils.message(ResponseCode.ORDER_UPDATE_FAILED, ResponseCode.ORDER_UPDATE_FAILED.getMessage());
        }
        return ResultUtils.success();
    }

    /**
     * 获取有效的订单信息
     *
     * @param uid 用户ID
     * @return 订单信息
     */
    private List<MealOrder> getValidOrder(Long uid, String orderSn) {
        MealOrderExample example = new MealOrderExample();
        example.createCriteria().andUserIdEqualTo(uid).andDeletedEqualTo(Boolean.FALSE).andOrderSnEqualTo(orderSn);
        return this.mealOrderMapper.selectByExample(example);
    }

    /**
     * 调用微信退款接口
     *
     * @return 微信退款结果
     */
    private WxPayRefundResult doWxRefund(String orderSn, BigDecimal actualPrice, MealOrderWxWithBLOBs mealOrderWx) {
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(orderSn);
        wxPayRefundRequest.setOutRefundNo("refund_" + orderSn);
        wxPayRefundRequest.setNotifyUrl(wxProperties.getRefundNotifyUrl());
        wxPayRefundRequest.setTotalFee(actualPrice.multiply(new BigDecimal(100)).intValue());
        wxPayRefundRequest.setRefundFee(wxPayRefundRequest.getTotalFee());
        mealOrderWx.setRequestParam(JsonUtils.toJsonKeepNullValue(wxPayRefundRequest));
        try {
            return wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            this.logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Result<?> detail(Long uid, String orderSn, Integer page, Integer limitVo) {
        var user = this.mealUserMapper.selectByPrimaryKey(uid);
        if (Objects.nonNull(orderSn)) { // 检查订单ID是否为空
            // 找到此笔订单的详细
            var validOrders = this.getValidOrder(uid, orderSn);
            if (ObjectUtils.isEmpty(validOrders)) { // 如果订单不存在，返回错误信息
                return ResultUtils.message(ResponseCode.ORDER_NOT_FIND, ResponseCode.ORDER_NOT_FIND.getMessage());
            }
            // 获取订单相关的用户和商店信息
            var shop = this.mealShopMapper.selectByPrimaryKey(validOrders.get(0).getShopId());
            var orderIds = validOrders.stream().map(MealOrder::getId).collect(Collectors.toList());

            // 查询订单商品和相关的灾害商品信息
            var orderGoodsExample = new MealOrderGoodsExample();
            orderGoodsExample.createCriteria().andOrderIdIn(orderIds);
            var listByGoods = this.mealOrderGoodsMapper.selectByExample(orderGoodsExample);
            var listByGoodsByOrderId = listByGoods.stream().collect(Collectors.groupingBy(MealOrderGoods::getOrderId));
            var calamityExample = new MealOrderGoodsCalamityExample();
            calamityExample.createCriteria().andOrderIdIn(orderIds);
            var listByCalamity = this.mealOrderGoodsCalamityMapper.selectByExample(calamityExample);
            // 将相关的灾害商品信息按照商品ID分组
            Map<Long, List<MealOrderGoodsCalamity>> goodsIdByCalamity = listByCalamity.stream().collect(Collectors.groupingBy(MealOrderGoodsCalamity::getOrderGoodsId));
            var vo = new OrderDetailsVo();
            vo.setNickName(user.getNickname());
            vo.setCount(1L);
            vo.setShipDate(validOrders.get(0).getShipTime().toLocalDate());
            vo.setCustomerPhone(user.getMobile());
            vo.setOrderStatus(validOrders.get(0).getOrderStatus());
            vo.setOrderStatusMessage(OrderStatusEnum.find(validOrders.get(0).getOrderStatus()).get().getMessage());
            vo.setMoney(validOrders.stream().map(MealOrder::getActualPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            vo.setShopName(shop.getName());
            vo.setShopPhone(shop.getPhone());
            vo.setOrders(validOrders.stream().map(e -> {
                var orderDetailSonVo = new OrderDetailSonVo();
                List<MealOrderGoods> mealOrderGoods = listByGoodsByOrderId.get(e.getId());
                orderDetailSonVo.setOrderDetailGoodsVos(mealOrderGoods.stream().map(a -> {
                    // 创建订单商品的VO对象
                    var goodsVo = new OrderDetailGoodsVo();
                    goodsVo.setGoodsMoney(a.getPrice());
                    goodsVo.setGoodsName(a.getGoodsName());
                    goodsVo.setUnit(a.getUnit());
                    goodsVo.setGoodsNumber(a.getNumber());
                    // 如果商品存在灾害商品信息，创建订单商品灾害的VO对象
                    var calamities = goodsIdByCalamity.get(a.getId());
                    if (ObjectUtils.isEmpty(calamities)) {
                        return goodsVo;
                    } else {
                        goodsVo.setOrderDetailCalamityVos(calamities.stream().map(b -> new OrderDetailCalamityVo().setCalamityNumber(b.getNumber()).setCalamityName(b.getCalamityName()).setUnit(b.getUnit()).setCalamityMoney(b.getPrice())).collect(Collectors.toList()));
                    }
                    return goodsVo;
                }).collect(Collectors.toList()));
                orderDetailSonVo.setCount((long) mealOrderGoods.size());
                orderDetailSonVo.setMoney(e.getActualPrice());
                orderDetailSonVo.setShipSn(e.getOrderStatus().compareTo(OrderStatusEnum.COMPLETED.getMapping()) == 0 ? e.getShipSn() : "");
                orderDetailSonVo.setIsTimeOnSale(e.getIsTimeOnSale());
                return orderDetailSonVo;
            }).collect(Collectors.toList()));
            return ResultUtils.success(vo);
        } else {
            var example = new MealOrderExample();
            example.createCriteria().andUserIdEqualTo(uid).andDeletedEqualTo(Boolean.FALSE);
            // 按订单添加时间倒序排序
            example.setOrderByClause("add_time DESC");
            // 通过 mealOrderMapper 的 selectByExample 方法查询订单列表
            List<MealOrder> mealOrders = this.mealOrderMapper.selectByExample(example);
            if (mealOrders.isEmpty()) {
                return ResultUtils.successWithEntities(Collections.emptyList(), 0L);
            }
            var shopMap = MapperUtils.shopMapByIds(mealOrders.stream().map(MealOrder::getShopId).distinct().collect(Collectors.toList()), mealShopMapper);
            var ordersMap = mealOrders.stream().collect(Collectors.groupingBy(MealOrder::getOrderSn));
            List<OrderRecordVo> listVo = ordersMap.keySet().stream().map(e -> {
                var orders = ordersMap.get(e);
                var actualPrice = orders.stream().map(MealOrder::getActualPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                MealOrder order = orders.get(0);
                var shop = shopMap.get(order.getShopId());
                var vo = new OrderRecordVo();
                // 设置订单的实际价格
                vo.setMoney(actualPrice);
                vo.setShipDate(order.getShipTime().toLocalDate());
                vo.setShopPhone(shop.getPhone());
                vo.setCustomerPhone(user.getMobile());
                // 设置订单的添加时间
                vo.setOrderDate(order.getAddTime());
                vo.setOrderStatus(order.getOrderStatus());
                vo.setOrderStatusMessage(OrderStatusEnum.find(order.getOrderStatus()).get().getMessage());
                // 设置订单的 id
                vo.setOrderSn(e);
                // 设置店铺的头像 URL
                vo.setShopAvatar(shop.getPicUrl());
                // 设置店铺的名称
                vo.setShopName(shop.getName());
                orders.forEach(a -> {
                    String shipSn = a.getOrderStatus().compareTo(OrderStatusEnum.COMPLETED.getMapping()) == 0 ? a.getShipSn() : "";
                    Arrays.stream(IsTimeSaleEnum.values()).filter(isTimeSaleEnum -> isTimeSaleEnum.is(a.getIsTimeOnSale())).findFirst().ifPresent(isTimeSaleEnum -> {
                        switch (isTimeSaleEnum) {
                            case BREAKFAST:
                                vo.setShipSnByBreakFast(shipSn);
                                break;
                            case LUNCH:
                                vo.setShipSnByLunch(shipSn);
                                break;
                            case DINNER:
                                vo.setShipSnByDinner(shipSn);
                                break;
                            default:
                                break;
                        }
                    });
                });
                return vo;
            }).sorted(Comparator.comparing(OrderRecordVo::getOrderDate).reversed()).skip(limitVo != null && page != null ? (long) limitVo * (page - 1) : 0).limit(limitVo != null ? limitVo : ordersMap.size()).collect(Collectors.toList());
            return ResultUtils.successWithEntities(listVo, (long) ordersMap.size());
        }
    }


    /**
     * 处理餐品订单的微信支付预支付操作
     *
     * @param user 餐品订单的用户
     * @return 支付结果
     */
    private Result<?> prepaySon(MealUser user, String orderSn, BigDecimal actualPrice,String message) {
        // 创建支付记录对象并设置初始值
        MealOrderWxWithBLOBs log = new MealOrderWxWithBLOBs();
        log.setOrderType("ORDER");
        log.setOrderSn(orderSn);
        // 记录支付请求参数并输出日志
        this.logger.info("用户:{},订单编号:{},正在调取微信支付", JsonUtils.toJson(user), orderSn);
        // 获取用户的微信OpenID
        String wxOpenid = user.getWxOpenid();
        if (Objects.isNull(wxOpenid)) {
            // 如果OpenID为空，返回错误结果
            return ResultUtils.message(ResponseCode.AUTH_OPENID_UNACCESS, ResponseCode.AUTH_OPENID_UNACCESS.getMessage());
        }
        // 调用微信支付服务创建订单，并将结果存入result变量
        WxPayMpOrderResult result;
        try {
            // 创建微信统一下单请求对象，并设置各个参数
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(orderSn); // 设置商户订单号
            orderRequest.setOpenid(wxOpenid); // 设置用户的微信OpenID
            orderRequest.setBody("订单：" + orderSn); // 设置订单描述
            // 将元转换成分
            int fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee); // 设置订单总金额（单位为分）
            orderRequest.setSpbillCreateIp(user.getLastLoginIp()); // 设置客户端IP
            log.setRequestParam(JsonUtils.toJsonKeepNullValue(orderRequest)); // 记录请求参数
            result = wxPayService.createOrder(orderRequest); // 调用微信支付服务创建订单
        } catch (Exception e) {
            // 如果创建订单失败，返回错误结果
            e.printStackTrace();
            return ResultUtils.message(ResponseCode.ORDER_PAY_FAIL, ResponseCode.ORDER_PAY_FAIL.getMessage());
        }
        var example = new MealOrderExample();
        example.createCriteria().andOrderSnEqualTo(orderSn);
        var order = new MealOrder();
        order.setOrderStatus(OrderStatusEnum.PAYING.getMapping());
        order.setMessage(message);
        if (this.mealOrderMapper.updateByExampleSelective(order, example) < 1) {
            return ResultUtils.message(ResponseCode.ORDER_PAY_FAIL, ResponseCode.ORDER_PAY_FAIL.getMessage());
        }
        // 记录支付响应结果并将记录对象存入数据库
        log.setResponseParam(JsonUtils.toJsonKeepNullValue(result));
        this.mealOrderWxMapper.insertSelective(log);

        // 返回支付结果
        return ResultUtils.success(result);
    }

    //    private Result<?> combineSon(MealUser user, List<MealOrder> orders) {
//        // 创建支付记录对象并设置初始值
//        MealOrderWx log = new MealOrderWx();
//        log.setOrderType("ORDER");
//
//        // 记录支付请求参数并输出日志
//        this.logger.info("订单:{},用户:{},正在调取微信支付", JsonUtils.toJson(user), JsonUtils.toJson(order));
//
//        // 获取用户的微信OpenID
//        String wxOpenid = user.getWxOpenid();
//        if (Objects.isNull(wxOpenid)) {
//            // 如果OpenID为空，返回错误结果
//            return ResultUtils.message(ResponseCode.AUTH_OPENID_UNACCESS, ResponseCode.AUTH_OPENID_UNACCESS.getMessage());
//        }
//
//        // 调用微信支付服务创建订单，并将结果存入result变量
//        WxPayMpOrderResult result;
//        CombineTransactionsRequest request;
//        List<CombineTransactionsRequest.SubOrders> subOrders = request.getSubOrders();
//
//        orders.stream().map(e->{
//            CombineTransactionsRequest.SubOrders  order = new CombineTransactionsRequest.SubOrders();
//            int fee = e.getActualPrice().multiply(new BigDecimal(100)).intValue();
//        })
//        try {
//            // 创建微信统一下单请求对象，并设置各个参数
//            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
//            orderRequest.setOutTradeNo(order.getOrderSn()); // 设置商户订单号
//            orderRequest.setOpenid(wxOpenid); // 设置用户的微信OpenID
//            orderRequest.setBody("订单：" + order.getOrderSn()); // 设置订单描述
//            // 将元转换成分
//            int fee = order.getActualPrice().multiply(new BigDecimal(100)).intValue();
//            orderRequest.setTotalFee(fee); // 设置订单总金额（单位为分）
//            orderRequest.setSpbillCreateIp(user.getLastLoginIp()); // 设置客户端IP
//            log.setRequestParam(JsonUtils.toJsonKeepNullValue(orderRequest)); // 记录请求参数
//            result = wxPayService.createOrder(orderRequest); // 调用微信支付服务创建订单
//        } catch (Exception e) {
//            // 如果创建订单失败，返回错误结果
//            e.printStackTrace();
//            return ResultUtils.message(ResponseCode.ORDER_PAY_FAIL, ResponseCode.ORDER_PAY_FAIL.getMessage());
//        }
//
//        // 记录支付响应结果并将记录对象存入数据库
//        log.setResponseParam(JsonUtils.toJsonKeepNullValue(result));
//        this.mealOrderWxMapper.insertSelective(log);
//
//        // 返回支付结果
//        return ResultUtils.success(result);
//    }
    @Override
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        // 从 request 中读取微信支付平台的支付通知
        this.logger.info("微信回调:{}", request.toString());

        String xmlResult;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            this.logger.error("获取支付通知失败", e);
            // 返回通知失败
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        WxPayOrderNotifyResult result = null;
        try {
            // 解析微信支付平台的支付通知
            result = wxPayService.parseOrderNotifyResult(xmlResult);
            this.logger.info("微信回调报文:{}", JsonUtils.toJson(result));
            // 检查支付通知的返回结果和业务结果，如果不成功则记录错误日志并抛出异常
            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getResultCode()) || !WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())) {
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
        } catch (WxPayException e) {
            logger.error("解析支付通知失败", e);
            // 返回通知失败
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        // 处理支付通知，更新订单状态
        String orderSn = result.getOutTradeNo(); // 获取订单号
        this.logger.info("处理腾讯支付平台的订单支付回调 order:{}结果:{}", orderSn, result);
        String payId = result.getTransactionId(); // 获取微信支付订单号
        // 将分转换为元
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());

        // 根据订单号查询订单信息
        var mealOrders = this.selectOrderBySn(orderSn);
        BigDecimal actualPrice = mealOrders.stream().map(MealOrder::getActualPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        var mealOrder = mealOrders.get(0);
        // 创建支付记录对象并设置初始值
        MealOrderWxWithBLOBs log = new MealOrderWxWithBLOBs();
        // 记录支付请求参数并输出日志
        if (ObjectUtils.isEmpty(mealOrders)) {
            this.logger.error("订单不存在 sn={}", orderSn);
            // 订单不存在，返回通知失败
            return WxPayNotifyResponse.fail("订单不存在 sn=" + orderSn);
        }
        log.setOrderType("ORDER_NOTIFY");
        log.setOrderSn(mealOrder.getOrderSn());
        log.setResponseParam(JsonUtils.toJson(result));
        if (OrderStatusEnum.notPayed(mealOrder)) {
            this.logger.info("订单已经处理成功sn={}", orderSn);
            // 如果订单已经处理成功，则直接返回通知成功
            return WxPayNotifyResponse.fail("订单不能支付!");
        }
        this.logger.info("微信订单回调接口 找到库里原始订单:{}", orderSn);
        // 检查支付订单金额是否正确，如果不正确则返回通知失败
        if (!totalFee.equals(actualPrice.toString())) {
            logger.error("sn={}支付金额不符合 totalFe e={}", orderSn, totalFee);
            return WxPayNotifyResponse.fail(mealOrder.getOrderSn() + " : 支付金额不符合 totalFee=" + totalFee);
        }
        //发放取餐码
        shipProcess(mealOrders, payId);
        // 更新订单信息到数据库
        LinkedList<Function<Void, Integer>> functions = new LinkedList<>();
        mealOrders.forEach(e -> {
            functions.add(nothing -> mealOrderMapper.updateByPrimaryKeySelective(e));
        });
        if (this.transactionExecutor.transaction(functions)) {
            //清空购物车
            this.logger.info("此订单支付成功:{}", mealOrder.getOrderSn());
            this.mealOrderWxMapper.insertSelective(log);
            return WxPayNotifyResponse.success("处理成功!");
        }
        this.logger.info("订单号:{} 更新已支付状态失败", orderSn);
        return WxPayNotifyResponse.fail("订单更新失败!");
    }

    @Override
    public Object refundNotify(String xmlResult) {
        WxPayRefundNotifyResult result;
        try {
            result = wxPayService.parseRefundNotifyResult(xmlResult);
            this.logger.info("微信退款回调:{}", JsonUtils.toJson(result));
            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())) {
                throw new WxPayException("微信退款-通知失败！");
            }
            if (!WxPayConstants.RefundStatus.SUCCESS.equals(result.getReqInfo().getRefundStatus())) {
                throw new WxPayException("微信退款-通知失败");
            }
        } catch (WxPayException e) {
            logger.error("微信退款-通知失败:", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
        WxPayRefundNotifyResult.ReqInfo reqInfo = result.getReqInfo();
        var orderSn = reqInfo.getOutTradeNo();
        // 退款成功业务处理
        var mealWx = new MealOrderWxWithBLOBs();
        mealWx.setOrderSn(orderSn);
        mealWx.setOrderType("REFUND_NOTIFY");
        mealWx.setResponseParam(JsonUtils.toJson(result));
        var orderNew = new MealOrder();
        orderNew.setOrderStatus(OrderStatusEnum.REFUNDED_OK.getMapping());
        orderNew.setOrderSn(orderSn);
        orderNew.setEndTime(LocalDateTime.now());
        orderNew.setRefundStatus((short) 1);
        this.mealOrderWxMapper.insertSelective(mealWx);
        // 更新订单信息
        if (this.mealOrderMapper.updateByOrderSn(orderNew) < 1) {
            return ResultUtils.message(ResponseCode.ORDER_UPDATE_FAILED, ResponseCode.ORDER_UPDATE_FAILED.getMessage());
        }
        this.logger.info("单号:{}微信退款回调成功", orderSn);
        return WxPayNotifyResponse.success("OK");
    }

    private void shipProcess(List<MealOrder> orders, String payId) {
        orders.forEach(e -> {
            e.setPayId(payId);
            e.setPayTime(LocalDateTime.now());
            e.setOrderStatus(OrderStatusEnum.PAID.getMapping());
            e.setShipSn(OrderSnUtils.generateOrderShipSn(e.getIsTimeOnSale(), mealOrderMapper));
        });
    }

    private List<MealOrder> selectOrderBySn(String orderSn) {
        var example = new MealOrderExample();
        example.createCriteria().andOrderSnEqualTo(orderSn).andDeletedEqualTo(Boolean.FALSE);
        return this.mealOrderMapper.selectByExample(example);
    }


}







