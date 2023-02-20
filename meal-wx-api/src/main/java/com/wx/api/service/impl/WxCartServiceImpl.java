package com.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealCartExample;
import com.meal.common.mapper.MealCartMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxShoppingCartVo;
import com.meal.common.transaction.TransactionExecutor;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.wx.api.service.WxCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Function;
@Service
public class WxCartServiceImpl implements WxCartService {
    @Resource
    private Validator validator;
    @Resource
    private MealCartMapper mealCartMapper;

    @Resource
    private MealShopMapper mealShopMapper;

    @Resource
    private TransactionExecutor transactionExecutor;
    private final Logger logger = LoggerFactory.getLogger(WxCartServiceImpl.class);

    @Override
    public Result<?> insertRqSuperShoppingCart(WxShoppingCartVo shoppingCartVo, Long uid) {
        {
            Set<ConstraintViolation<WxShoppingCartVo>> violations = this.validator.validate(shoppingCartVo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][insertRqSuperShoppingCart]:param.  request: {}, violation: {}",
                        shoppingCartVo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shoppingCartVo.getShopId(), Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][insertRqSuperShoppingCart]:store. uid: {}, request: {}",
                        SecurityUtils.getUserId(), shoppingCartVo);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        List<Function<Void, Integer>> functions = new LinkedList<>();
        var example = new MealCartExample();
        example.createCriteria().andUserIdEqualTo(uid);
        //刪除用戶收藏數據
        functions.add(nothing -> {
            mealCartMapper.deleteByExample(example);
            return 1;
        });
        var products= shoppingCartVo.getGoods();
        Collections.reverse(products);

        if (this.transactionExecutor.transaction(functions)) {
            return ResultUtils.success();
        }
        return null;
    }
}
