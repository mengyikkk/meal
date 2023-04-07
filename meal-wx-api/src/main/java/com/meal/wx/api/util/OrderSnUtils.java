package com.meal.wx.api.util;

import com.meal.common.dto.MealOrderExample;
import com.meal.common.mapper.MealOrderMapper;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderSnUtils {

    public static String generateOrderSn(String prefix,MealOrderMapper mealOrderMapper) {
        // 拼接订单号
        do {
            int length = 18;
            // 随机字符串
            String randomStr = RandomStringUtils.randomAlphanumeric(length - prefix.length());
            String orderSn = prefix + IntStream.range(0, length - prefix.length())
                    .mapToObj(i -> String.valueOf(randomStr.charAt(i)))
                    .collect(Collectors.joining());
            if (!orderSn(mealOrderMapper,orderSn)) {
                return orderSn;
            }
        } while (true);
    }

    public static String generateOrderShipSn(Integer prefix,MealOrderMapper mealOrderMapper) {
        // 拼接订单号
        do {
            String orderSn = prefix.toString()+ (new Random().nextInt(900) + 100);
            if (!shipSn(mealOrderMapper,orderSn)) {
                return orderSn;
            }
        } while (true);
    }

    public static Boolean shipSn(MealOrderMapper mealOrderMapper, String sn) {
        return mealOrderMapper.hasOrderByShip(sn);
    }
    public static Boolean orderSn(MealOrderMapper mealOrderMapper, String sn) {
        return mealOrderMapper.hasOrderByShip(sn);
    }
}
