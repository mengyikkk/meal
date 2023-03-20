package com.meal.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.WxOrderVo;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealLittleCalamityMapper;
import com.meal.common.model.OrderCartCalamityVo;
import com.meal.common.model.OrderCartVo;
import com.meal.common.utils.MapperUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.wx.api.service.WxOrderService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WxOrderServiceImpl implements WxOrderService {
    @Resource
    private Validator validator;
    @Resource
    private MealGoodsMapper mealGoodsMapper;

    @Resource
    private MealLittleCalamityMapper mealLittleCalamityMapper;
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
        var goodListVo = wxOrderVo.getShoppingCartVos();
        var shopId = wxOrderVo.getShopId();
        List<Long> goodListVoIds = goodListVo.stream().map(OrderCartVo::getGoodsId).collect(Collectors.toList());
        var goodsByShopMap = MapperUtils.goodsMapByShop(mealGoodsMapper, wxOrderVo.getShopId(), goodListVoIds);
        Set<Long> goodsByShopIds = goodsByShopMap.keySet();
        var calamitiesMap = MapperUtils.calamityMapByShopAndGoods(mealLittleCalamityMapper
                , Collections.emptyList(), new ArrayList<>(goodsByShopIds), shopId);

        BigDecimal goodsPrice = BigDecimal.ZERO;
        for (OrderCartVo shoppingCartVo : goodListVo) {
            var good = goodsByShopMap.get(shoppingCartVo.getGoodsId());
            if (Objects.isNull(good)) {
                return ResultUtils.message(ResponseCode.GOODS_INVALID, ResponseCode.GOODS_INVALID.getMessage());
            }
            if (good.getRetailPrice().compareTo(shoppingCartVo.getPrice()) != 0) {
                return ResultUtils.message(ResponseCode.GOODS_INVALID, ResponseCode.GOODS_INVALID.getMessage());
            }
            goodsPrice = goodsPrice.add(good.getRetailPrice().multiply(BigDecimal.valueOf(shoppingCartVo.getNumber())));
            var calamityVos = shoppingCartVo.getOrderCartCalamityVos();
            if (ObjectUtils.isNotEmpty(calamityVos)) {
                //check 小料的价格
                for (OrderCartCalamityVo calamityVo : calamityVos) {
                    var calamity = calamitiesMap.get(calamityVo.getCalamityId());
                    if (Objects.isNull(calamity)){
                        return ResultUtils.message(ResponseCode.CALAMITY_IS_INVALID,ResponseCode.CALAMITY_IS_INVALID.getMessage());
                    }
                    if (calamity.getRetailPrice().compareTo(calamityVo.getCalamityPrice())!=0){
                        return ResultUtils.message(ResponseCode.CALAMITY_IS_INVALID,ResponseCode.CALAMITY_IS_INVALID.getMessage());
                    }
                    goodsPrice = goodsPrice.add(calamity.getRetailPrice().multiply(BigDecimal.valueOf(calamityVo.getCalamityNumber())));
                }
            }
        }
        if (goodsPrice.compareTo(wxOrderVo.getActualPrice())!=0){
            return ResultUtils.message(ResponseCode.ORDER_CHECKOUT_FAIL,ResponseCode.ORDER_CHECKOUT_FAIL.getMessage());
        }

        return null;
    }

    private void createOrder(WxOrderVo wxOrderVo) {

    }
}
