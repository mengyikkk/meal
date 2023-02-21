package com.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealCart;
import com.meal.common.dto.MealCartExample;
import com.meal.common.mapper.MealCartMapper;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxShopCartResponseVo;
import com.meal.common.model.WxShoppingCartVo;
import com.meal.common.transaction.TransactionExecutor;
import com.meal.common.utils.MapperUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.wx.api.service.WxCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WxCartServiceImpl implements WxCartService {
    @Resource
    private Validator validator;
    @Resource
    private MealCartMapper mealCartMapper;
    @Resource
    private MealGoodsMapper mealGoodsMapper;
    @Resource
    private MealShopMapper mealShopMapper;

    @Resource
    private TransactionExecutor transactionExecutor;
    private final Logger logger = LoggerFactory.getLogger(WxCartServiceImpl.class);

    @Override
    public Result<?> insertShoppingCart(WxShoppingCartVo shoppingCartVo, Long uid) {
        {
            Set<ConstraintViolation<WxShoppingCartVo>> violations = this.validator.validate(shoppingCartVo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][insertShoppingCart]:param.  request: {}, violation: {}",
                        shoppingCartVo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shoppingCartVo.getShopId(), Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][insertShoppingCart]:store. uid: {}, request: {}",
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
        var goodsByShopMap = MapperUtils.goodsMapByShop(mealGoodsMapper, shoppingCartVo.getShopId());
        var products = shoppingCartVo.getGoods();
        Collections.reverse(products);
        products.forEach(e -> {
                    var cart = new MealCart();
                    cart.setGoodsId(e.getGoodsId());
                    cart.setNumber(e.getNumber());
                    cart.setShopId(shoppingCartVo.getShopId());
                    cart.setGoodsSn(e.getGoodsSn());
                    cart.setChecked(e.getChecked());
                    //todo 有可能空指针 和前端判断一下 消息共同事宜 决定
                    cart.setGoodsName(goodsByShopMap.get(e.getGoodsId()).getName());
                    cart.setUserId(uid);
                    cart.setAddTime(LocalDateTime.now());
                    cart.setUpdateTime(LocalDateTime.now());
                    functions.add(nothing ->
                            mealCartMapper.insertSelective(cart)
                    );
                }
        );
        if (this.transactionExecutor.transaction(functions)) {
            return ResultUtils.success();
        }
        return ResultUtils.unknown();
    }

    @Override
    public Result<?> selectShoppingCartAmount(Long uid, Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][selectShoppingCartAmount]:store. uid: {}, request: {}",
                        SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var example = new MealCartExample();
        example.createCriteria().andShopIdEqualTo(shopId).andUserIdEqualTo(uid).andDeletedEqualTo(MealCart.NOT_DELETED);
        return ResultUtils.success(this.mealCartMapper.countByExample(example));
    }

    @Override
    public Result<?> selectShoppingCartList(Long uid, Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][selectShoppingCartList]:store. uid: {}, request: {}",
                        SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var example = new MealCartExample();
        example.createCriteria().andShopIdEqualTo(shopId).andUserIdEqualTo(uid).andDeletedEqualTo(MealCart.NOT_DELETED);
        var goodsByShopMap = MapperUtils.goodsMapByShop(mealGoodsMapper, shopId);
        var result = this.mealCartMapper.selectByExample(example).stream().map(e -> {
            var vo = new WxShopCartResponseVo();
            var product = goodsByShopMap.getOrDefault(e.getGoodsId(), null);
            if (Objects.isNull(product)) {
                vo.setErrStatus(Boolean.TRUE);
                vo.setGoodsName(e.getGoodsName());
            } else {
                vo.setErrStatus(Boolean.FALSE);
                vo.setGoodsName(product.getName());
            }
            vo.setGoodsId(e.getGoodsId()).setGoodsName(e.getGoodsName())
                    .setGoodsIsTime(e.getGoodsIsTime()).setChecked(e.getChecked())
                    .setGoodsName(e.getGoodsName()).setNumber(e.getNumber()).setGoodsSn(e.getGoodsSn());
            return vo;
        }).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @Override
    public Result<?> deleteShoppingCartList(Long uid, Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][deleteShoppingCartList]:store. uid: {}, request: {}",
                        SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var example = new MealCartExample();
        example.createCriteria().andShopIdEqualTo(shopId).andUserIdEqualTo(uid).andDeletedEqualTo(MealCart.NOT_DELETED);
        if(this.mealCartMapper.deleteByExample(example)<1){
            return  ResultUtils.unknown();
        }
        return ResultUtils.success();
    }
}
