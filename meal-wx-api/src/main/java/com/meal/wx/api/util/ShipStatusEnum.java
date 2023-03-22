package com.meal.wx.api.util;

import com.meal.common.StateMapping;

public enum ShipStatusEnum implements StateMapping<Short> {
    NOT_SHIP((short) 0, "未取货"),
    SHIPPED((short) 1, "已取货");
    private final short code;
    private final String message;

    ShipStatusEnum(Short code, String message) {
        this.code = code;
        this.message = message;
    }



    @Override
    public Short getMapping() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }
}
