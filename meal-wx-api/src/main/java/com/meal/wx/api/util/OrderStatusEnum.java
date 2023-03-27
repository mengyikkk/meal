package com.meal.wx.api.util;

import com.meal.common.StateMapping;
import com.meal.common.dto.MealOrder;

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
    public static boolean notPayed(MealOrder order) {
        return OrderStatusEnum.PAID.is(order.getOrderStatus()) ||OrderStatusEnum.COMPLETED.is(order.getOrderStatus())
                || OrderStatusEnum.REFUNDED.is(order.getOrderStatus());
    }
    public static boolean isCanceled(MealOrder order) {
        return OrderStatusEnum.PAID.is(order.getOrderStatus()) ||OrderStatusEnum.UNPAID.is(order.getOrderStatus());
    }

}
