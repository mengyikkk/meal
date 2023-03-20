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
import java.util.Objects;
import java.util.Set;
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
        var goodsByShop = MapperUtils.goodsMapByShop(mealGoodsMapper, wxOrderVo.getShopId());
        var goodsByCalamity =MapperUtils.calamityMapByShopAndGoods(mealLittleCalamityMapper,
                goodListVo.stream().map(OrderCartVo::getGoodsId).collect(Collectors.toList()), wxOrderVo.getShopId());
        BigDecimal goodsPrice = BigDecimal.ZERO;
        for (OrderCartVo shoppingCartVo : goodListVo) {
            var good = goodsByShop.get(shoppingCartVo.getGoodsId());
            //check
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

                }
            }
        }
        return null;
    }

    private void createOrder(WxOrderVo wxOrderVo){

    }
}
