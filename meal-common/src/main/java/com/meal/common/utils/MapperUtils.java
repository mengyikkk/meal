package com.meal.common.utils;

import com.meal.common.dto.MealGoods;
import com.meal.common.dto.MealGoodsExample;
import com.meal.common.dto.MealLittleCalamity;
import com.meal.common.dto.MealLittleCalamityExample;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealLittleCalamityMapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        example.createCriteria().andShopIdIn(List.of(shopId,0L)).andDeletedEqualTo(MealGoods.Deleted.NOT_DELETED.value());
        return  mealGoodsMapper.selectByExample(example).stream().collect(Collectors.toMap(MealGoods::getId, Function.identity()));
    }
    public static Map<Long, MealGoods> goodsMapAllByShop(MealGoodsMapper mealGoodsMapper,Long shopId){
        var example  = new MealGoodsExample();
        example.createCriteria().andShopIdIn(List.of(shopId,0L));
        return  mealGoodsMapper.selectByExample(example).stream().collect(Collectors.toMap(MealGoods::getId, Function.identity()));
    }
    public static Map<Long, MealLittleCalamity> calamityMapByShopAndGoods(MealLittleCalamityMapper mealLittleCalamityMapper, List<Long> ids, Long shopIds){
        var example  = new MealLittleCalamityExample();
        example.createCriteria().andShopIdIn(List.of(shopIds,0L)).andIdIn(ids).andDeletedEqualTo(MealGoods.Deleted.NOT_DELETED.value());
        return  mealLittleCalamityMapper.selectByExample(example).stream().collect(Collectors.toMap(MealLittleCalamity::getId, Function.identity()));
    }
    //key:goodsId->对应的所有小料
    public static Map<Long, List<MealLittleCalamity>> goodsByCalamity(MealLittleCalamityMapper mealLittleCalamityMapper,Long shopId){
        var exampleCalamity = new MealLittleCalamityExample();
        exampleCalamity.createCriteria().andShopIdIn(List.of(0L, shopId))
                .andDeletedEqualTo(MealLittleCalamity.NOT_DELETED);
        return  mealLittleCalamityMapper.selectByExample(exampleCalamity).stream()
                .collect(Collectors.groupingBy(MealLittleCalamity::getGoodsId));
    }
    //key:shopId->对应的 所有商品和
    public static Map<Long, List<MealGoods>> shopByGoods(MealGoodsMapper mealGoodsMapper,Long shopId){
        var example  = new MealGoodsExample();
        example.createCriteria().andShopIdIn(List.of(shopId,0L)).andDeletedEqualTo(MealGoods.Deleted.NOT_DELETED.value());
        return  mealGoodsMapper.selectByExample(example).stream().collect(Collectors.groupingBy(MealGoods::getShopId));
    }
}
