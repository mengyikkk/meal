package com.meal.common.utils;

import com.meal.common.dto.*;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealLittleCalamityMapper;
import com.meal.common.mapper.MealShopMapper;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MapperUtils {
    private MapperUtils() {
    }

    public static Map<Long, MealGoods> goodsMapByShop(MealGoodsMapper mealGoodsMapper, Long shopId, List<Long> goodIds) {
        var example = new MealGoodsExample();
        MealGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdIn(List.of(shopId, 0L)).andDeletedEqualTo(MealGoods.Deleted.NOT_DELETED.value()).andIsOnSaleEqualTo(Boolean.TRUE);
        if (!ObjectUtils.isEmpty(goodIds)) {
            criteria.andIdIn(goodIds);
        }
        return mealGoodsMapper.selectByExample(example).stream().collect(Collectors.toMap(MealGoods::getId, Function.identity()));
    }

    public static Map<Integer,List<MealGoods>> checkGoodsByIsTimeOnSale(MealGoodsMapper mealGoodsMapper, Long shopId,
                                                            List<Long> goodIds) {
        var example = new MealGoodsExample();
        MealGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdIn(List.of(shopId, 0L)).andDeletedEqualTo(MealGoods.Deleted.NOT_DELETED.value()).andIdIn(goodIds).andIsOnSaleEqualTo(Boolean.TRUE);
        return mealGoodsMapper.selectByExample(example).stream().collect(Collectors.groupingBy(MealGoods::getIsTimeOnSale));
    }

    public static Map<Long, MealLittleCalamity> calamityMapByShopAndGoods(MealLittleCalamityMapper mealLittleCalamityMapper, List<Long> ids, List<Long> goodIds, Long shopIds) {
        var example = new MealLittleCalamityExample();
        MealLittleCalamityExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdIn(List.of(shopIds, 0L)).andGoodsIdIn(goodIds).andDeletedEqualTo(MealGoods.Deleted.NOT_DELETED.value()).andIsOnSaleEqualTo(Boolean.TRUE);
        if (!ObjectUtils.isEmpty(ids)) {
            criteria.andIdIn(ids);
        }
        return mealLittleCalamityMapper.selectByExample(example).stream().collect(Collectors.toMap(MealLittleCalamity::getId, Function.identity()));
    }

    public static Map<Long, MealShop> shopMapByIds(List<Long> ids, MealShopMapper mealShopMapper) {
        return mealShopMapper.selectByExample(new MealShopExample().createCriteria().andIdIn(ids).example()).stream().collect(Collectors.toMap(MealShop::getId, Function.identity()));
    }
}
