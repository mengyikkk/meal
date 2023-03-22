package com.meal.wx.api.util;

import com.meal.common.StateMapping;

public enum RefundStatusEnum implements StateMapping<Short> {
    CAN_APPLY((short)0, "可申请"),
    USER_APPLIED((short)1, "用户已申请"),
    ADMIN_APPROVED((short)2, "管理员审核通过"),
    ADMIN_REFUNDED((short)3, "管理员退款成功"),
    ADMIN_REJECTED((short)4, "管理员审核拒绝"),
    USER_CANCELED((short)5, "用户已取消");
    private final short code;
    private final String message;

    RefundStatusEnum(Short code, String message) {
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
