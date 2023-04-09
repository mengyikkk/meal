package com.meal.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealCart;
import com.meal.common.dto.MealCartCalamity;
import com.meal.common.dto.MealCartCalamityExample;
import com.meal.common.dto.MealCartExample;
import com.meal.common.dto.MealGoods;
import com.meal.common.dto.MealLittleCalamity;
import com.meal.common.mapper.MealCartCalamityMapper;
import com.meal.common.mapper.MealCartMapper;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealLittleCalamityMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.*;
import com.meal.common.transaction.TransactionExecutor;
import com.meal.common.utils.MapperUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxCartService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WxCartServiceImpl implements WxCartService {
    @Resource
    private Validator validator;
    @Resource
    private MealCartMapper mealCartMapper;
    @Resource
    private  WxCartService wxCartService;
    @Resource
    private MealGoodsMapper mealGoodsMapper;
    @Resource
    private MealShopMapper mealShopMapper;
    @Resource
    private MealLittleCalamityMapper mealLittleCalamityMapper;
    @Resource
    private MealCartCalamityMapper mealCartCalamityMapper;
    @Resource
    private TransactionExecutor transactionExecutor;
    private final Logger logger = LoggerFactory.getLogger(WxCartServiceImpl.class);

    @Override
    public Result<?> insertShoppingCart(WxShoppingCartVo shoppingCartVo, Long uid) {
        {
            Set<ConstraintViolation<WxShoppingCartVo>> violations = this.validator.validate(shoppingCartVo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][insertShoppingCart]:param.  request: {}, violation: {}", shoppingCartVo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shoppingCartVo.getShopId(), Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][insertShoppingCart]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shoppingCartVo);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        List<Long> cartGoodIds = shoppingCartVo.getGoods().stream().map(ShoppingCartVo::getGoodsId).collect(Collectors.toList());
        //取出数据库有效的商品和小料
        Map<Long, MealGoods> longMealGoodsMap = MapperUtils.goodsMapByShop(mealGoodsMapper, shoppingCartVo.getShopId(), cartGoodIds);
        //店铺小料集合 //商品对应小料集合
        var goodsByCalamity = MapperUtils.calamityMapByShopAndGoods(mealLittleCalamityMapper, Collections.emptyList(), new ArrayList<>(longMealGoodsMap.keySet()), shoppingCartVo.getShopId());

        List<Function<Void, Integer>> functions = new LinkedList<>();
        var example = new MealCartExample();
        example.createCriteria().andUserIdEqualTo(uid);
        functions.add(nothing -> {
            mealCartMapper.deleteByExample(example);
            return 1;
        });
        var products = shoppingCartVo.getGoods();
        Collections.reverse(products);
        for (ShoppingCartVo product : products) {
            MealGoods goods = longMealGoodsMap.getOrDefault(product.getGoodsId(), null);
            if (Objects.isNull(goods)) {
                return ResultUtils.entityMessage(ResponseCode.GOODS_INVALID, ResponseCode.GOODS_INVALID.getMessage(), product.getGoodsId());
            }
            var cart = new MealCart();
            cart.setGoodsId(goods.getId());
            cart.setNumber(product.getNumber());
            cart.setShopId(shoppingCartVo.getShopId());
            cart.setGoodsSn(goods.getGoodsSn());
            cart.setChecked(product.getChecked());
            cart.setGoodsName(goods.getName());
            cart.setUserId(uid);
            cart.setAddTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            functions.add(nothing -> mealCartMapper.insertSelective(cart));
            if (ObjectUtils.isNotEmpty(product.getCartCalamityVos())) {
                for (CartCalamityVo cartCalamityVo : product.getCartCalamityVos()) {
                    MealLittleCalamity littleCalamity = goodsByCalamity.getOrDefault(cartCalamityVo.getCalamityId(), null);
                    if (Objects.isNull(littleCalamity)) {
                        return ResultUtils.entityMessage(ResponseCode.CALAMITY_IS_INVALID, ResponseCode.CALAMITY_IS_INVALID.getMessage(), cartCalamityVo.getCalamityId());
                    }
                    var cartCalamity = new MealCartCalamity();
                    cartCalamity.setCalamityId(littleCalamity.getId());
                    cartCalamity.setCalamityNumber(cartCalamityVo.getCalamityNumber());
                    cartCalamity.setCalamityName(littleCalamity.getName());
                    functions.add(nothing -> mealCartCalamityMapper.insertSelective(cartCalamity.setCartId(cart.getId())));
                }
            }
        }
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
                this.logger.warn("Service-Cart[WxCartServiceImpl][selectShoppingCartAmount]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        return ResultUtils.success(this.count(uid, shopId));
    }

    @Override
    public Result<?> selectShoppingCartList(Long uid, Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][selectShoppingCartList]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var example = new MealCartExample();
        example.createCriteria().andShopIdEqualTo(shopId).andUserIdEqualTo(uid).andDeletedEqualTo(MealCart.NOT_DELETED);
        List<MealCart> mealCarts = this.mealCartMapper.selectByExample(example);
        if (mealCarts.isEmpty()) {
            return ResultUtils.success();
        }
        var cartCalamityExample = new MealCartCalamityExample();
        cartCalamityExample.createCriteria().andCartIdIn(mealCarts.stream().map(MealCart::getId).collect(Collectors.toList())).andDeletedEqualTo(MealCartCalamity.NOT_DELETED);
        List<MealCartCalamity> mealCartCalamities = this.mealCartCalamityMapper.selectByExample(cartCalamityExample);
        var cartCalamityMap = mealCartCalamities.stream().collect(Collectors.groupingBy(MealCartCalamity::getCartId));
        var goodsByShopMap = MapperUtils.goodsMapByShop(mealGoodsMapper, shopId, Collections.emptyList());
        //小料
        var calamityMap = MapperUtils.calamityMapByShopAndGoods(mealLittleCalamityMapper, mealCartCalamities.stream().map(MealCartCalamity::getCalamityId).collect(Collectors.toList()), new ArrayList<>(goodsByShopMap.keySet()), shopId);
        List<WxShopCartResponseVo> responseVos = mealCarts.stream().map(e -> {
            var vo = new WxShopCartResponseVo();
            var product = goodsByShopMap.getOrDefault(e.getGoodsId(), null);
            if (Objects.isNull(product)) {
                vo.setErrStatus(Boolean.TRUE);
                vo.setGoodsName(e.getGoodsName());
            } else {
                vo.setErrStatus(Boolean.FALSE);
                vo.setUnit(product.getUnit());
                vo.setUrl(product.getPicUrl());
                vo.setPrice(product.getRetailPrice());
                vo.setGoodsName(product.getName());
                vo.setIsTimeOnSale(product.getIsTimeOnSale());
            }
            var cartCalamityByCartId = cartCalamityMap.getOrDefault(e.getId(), Collections.emptyList());
            List<WxShopCartCalamitySonVo> resultCalamityVos = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(cartCalamityByCartId)) {
                for (MealCartCalamity mealCartCalamity : cartCalamityByCartId) {
                    var wxShopCartCalamitySon = new WxShopCartCalamitySonVo();
                    wxShopCartCalamitySon.setCalamityId(mealCartCalamity.getCalamityId());
                    wxShopCartCalamitySon.setCalamityName(mealCartCalamity.getCalamityName());
                    wxShopCartCalamitySon.setCalamityNumber(mealCartCalamity.getCalamityNumber());
                    var calamity = calamityMap.getOrDefault(mealCartCalamity.getCalamityId(), null);
                    if (Objects.isNull(calamity)) {
                        wxShopCartCalamitySon.setErrStatus(Boolean.TRUE);
                    } else {
                        wxShopCartCalamitySon.setCalamityUnit(calamity.getUnit());
                        wxShopCartCalamitySon.setCalamityUrl(calamity.getPicUrl());
                        wxShopCartCalamitySon.setCalamityPrice(calamity.getRetailPrice());
                        wxShopCartCalamitySon.setErrStatus(Boolean.FALSE);
                        wxShopCartCalamitySon.setCalamityName(calamity.getName());
                    }
                    resultCalamityVos.add(wxShopCartCalamitySon);
                }
                vo.setCalamityVos(resultCalamityVos);
            }
            return vo.setGoodsId(e.getGoodsId()).setGoodsName(e.getGoodsName()).setGoodsIsTime(e.getGoodsIsTime()).setChecked(e.getChecked()).setGoodsName(e.getGoodsName()).setNumber(e.getNumber()).setGoodsSn(e.getGoodsSn());
        }).collect(Collectors.toList());
        var resultMap = responseVos.stream().filter(c->Objects.nonNull(c.getIsTimeOnSale())).collect(Collectors.groupingBy(WxShopCartResponseVo::getIsTimeOnSale));
        var result =resultMap.keySet().stream().map(e->{
            var vo = new WxShoppingCartOrderVo();
            vo.setGoods(resultMap.get(e));
            vo.setIsTimeOnSale(e);
            return vo;
        }).collect(Collectors.toList());
        var resultVo = new WxCartResponseAllVo().setErrGoods(responseVos.stream().filter(e->Objects.isNull(e.getIsTimeOnSale())).collect(Collectors.toList()))
                .setOrders(result).setCounts(this.count(uid, shopId));
        return ResultUtils.success(resultVo);
    }

    @Override
    public Result<?> deleteShoppingCartList(Long uid, Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Cart[WxCartServiceImpl][deleteShoppingCartList]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        this.logger.info("[WxCartServiceImpl][deleteShoppingCartList]:store. uid: {} 清空购物车, ", uid);
        wxCartService.deleteShoppingCart(uid, shopId);
        return ResultUtils.success();
    }

    private long count(Long uid, Long shopId) {
        var example = new MealCartExample();
        example.createCriteria().andShopIdEqualTo(shopId).andUserIdEqualTo(uid).andDeletedEqualTo(MealCart.NOT_DELETED);
        return this.mealCartMapper.countByExample(example);
    }

    @Transactional
    @Override
    public void deleteShoppingCart(Long uid, Long shopId) {
        var example = new MealCartExample();
        example.createCriteria().andShopIdEqualTo(shopId).andUserIdEqualTo(uid).andDeletedEqualTo(MealCart.NOT_DELETED);
        var exampleCalamity = new MealCartCalamityExample();
        List<Long> cartIds = this.mealCartMapper.selectByExample(example).stream().map(MealCart::getId).collect(Collectors.toList());
        if (ObjectUtils.isNotEmpty(cartIds)) {
            this.mealCartCalamityMapper.deleteByExample(exampleCalamity);
        }
        this.mealCartMapper.deleteByExample(example);
    }
}
