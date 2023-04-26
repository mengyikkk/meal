package com.meal.wx.api.util;

import com.meal.common.StateMapping;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum IsTimeSaleEnum implements StateMapping<Integer> {
    BREAKFAST(1,"早餐"),
    LUNCH(2,"午餐"),
    DINNER(3,"晚餐"),
    ;
    private final Integer code;
    private final String message;

    IsTimeSaleEnum(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getMapping() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }
    public static Optional<IsTimeSaleEnum> find(Integer code){
        Objects.requireNonNull(code);
        return Arrays.stream(IsTimeSaleEnum.values()).filter(entity-> entity.is(code)).findFirst();
    }
}
