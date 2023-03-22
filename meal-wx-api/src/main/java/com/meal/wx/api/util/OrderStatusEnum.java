package com.meal.wx.api.util;

import com.meal.common.StateMapping;

public enum OrderStatusEnum implements StateMapping<Short> {
    UNPAID((short) 0, "未支付"),
    PAID((short) 1, "已支付"),
    COMPLETED((short) 3, "订单完成"),
    REFUNDED((short) 4, "订单退款");
    private final short code;
    private final String message;

    OrderStatusEnum(Short code, String message) {
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
