package com.meal.wx.api.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public  class OrderSnUtils {

    public  static String generateOrderSn(String prefix) {
        // 固定前缀
        // 订单号长度
        int length = 18;
        // 随机字符串
        String randomStr = RandomStringUtils.randomAlphanumeric(length - prefix.length());
        // 拼接订单号
        return prefix + IntStream.range(0, length - prefix.length())
                .mapToObj(i -> String.valueOf(randomStr.charAt(i)))
                .collect(Collectors.joining());
    }

}
