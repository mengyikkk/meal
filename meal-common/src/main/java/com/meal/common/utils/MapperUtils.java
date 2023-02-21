package com.meal.common.utils;

import com.meal.common.dto.MealGoods;
import com.meal.common.dto.MealGoodsExample;
import com.meal.common.mapper.MealGoodsMapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public  final class MapperUtils {
    private MapperUtils(){}

    public static Map<Long, MealGoods> goodsMap(MealGoodsMapper mealGoodsMapper){
        var example  = new MealGoodsExample();
        example.createCriteria();
        return  mealGoodsMapper.selectByExample(example).stream().collect(Collectors.toMap(MealGoods::getId, Function.identity()));
    }
    public static Map<Long, MealGoods> goodsMapByShop(MealGoodsMapper mealGoodsMapper,Long shopId){
        var example  = new MealGoodsExample();
        example.createCriteria().andShopIdIn(List.of(0L,shopId)).andDeletedNotEqualTo(MealGoods.Deleted.IS_DELETED.value());
        return  mealGoodsMapper.selectByExample(example).stream().collect(Collectors.toMap(MealGoods::getId, Function.identity()));
    }
}
