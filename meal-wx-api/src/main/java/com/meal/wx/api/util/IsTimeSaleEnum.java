package com.meal.wx.api.util;

import com.meal.common.StateMapping;

public enum IsTimeSaleEnum implements StateMapping<Integer> {
    BREAKFAST(1),
    LUNCH(2),
    DINNER(3),

    ;
    private final Integer code;

    IsTimeSaleEnum(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getMapping() {
        return this.code;
    }
}
