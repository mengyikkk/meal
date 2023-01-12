package com.meal.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum ResponseCode implements StateMapping<Integer> {

    /** 成功 */
    SUCCESS(0),


    /** 服务器已经成功接受请求，但不会立即返回。或许会在某个时间回调客户端 */
    ASYNC_CALLBACK(2010),

    /** 当前帐户同时被登录了两次 */
    OTHER_TOKEN_EXISTS(2040),

    /** 当前帐户需要重新设置密码 */
    RESET_PASSWORD(2050),

    /** 访问频率过快 */
    ACCESS_LIMIT(4010),

    /** 帐户被禁用 */
    ACCOUNT_DISABLE(4020),

    /** 用户名或密码错误 */
    ACCOUNT_NOT_EXISTS(4030),

    /** 无效的身份标识 */
    TOKEN_ILLEGAL(4040),

    /** 没有找到匹配的实体信息 */
    ENTITY_NOT_FOUND(4050),

    /** 参数为null */
    PARAMETER_IS_NULL(4060),

    /** 存在 */
    THERE_ARE(4070),

    /** 参数非法 */
    PARAMETER_ILLEGAL(4080),

    /** 超时 */
    TIME_OUT(4090),

    /**金额限制*/
    AMOUNT_LIMIT(5030),

    /**两次密码不一致*/
    WRONG_PASSWORD_TWICE(5060),

    /**会员信息异常*/
    MEMBER_INFO_UNUSUAL(5080),

    /** 并发修改
     * 返回此状态码通常表示某一个DB操作通过了参数及权限检查。
     * 但DB操作静默失败，并且失败的原因是为了保证逻辑的冥等性。
     */
    CONCURRENT_MODIFICATION(6010),
    /** 强制更新 */
    FORCED_UPDATE(1121),
    /** ADDRESS更新失败 */
    ADDRESS_UPDATE_FAILED(1108),
    /** ADDRESS插入失败 */
    ADDRESS_INSERT_FAILED(1122),
    /** USERINFO更新失败 */
    USERINFO_UPDATE_FAILED(1123),
    /** USERINFO插入失败 */
    USERINFO_INSERT_FAILED(1124),
    /** COLLECT收藏失败 */
    COLLECT_INSERT_FAILED(1125),

    /** 配送方式不支持或无效 */
    DELIVERY_TYPE_ERR(1126),

    /** 支付方式不支持或无效 */
    PAY_TYPE_ERR(1127),

    /** ADDRESS无效 */
    ADDRESS_ERR(1128),
    /** PRODUCT数量错误 */
    PRODUCT_AMOUNT_ERR(1129),
    /** PRODUCT_ERR商品错误 */
    PRODUCT_ERR(1130),
    /** PRODUCT_PRICE_ERR */
    PRODUCT_PRICE_ERR(1131),
    /** 商品表插入错误 */
    PRODUCT_INSERT_FAILED(1132),
    /** CART_DELETE_FAILED */
    CART_DELETE_FAILED(1133),
    /** 减商品表出错 */
    REDUCE_STOCK_FAILED(1134),
    /** 该账户没有这个信用卡信息 */
    CARD_IS_NULL(1135),
    /** 信用卡错误 */
    CARD_IS_ERR(1136),
    /** 订单商品表插入出错 */
    PRODUCT_ORDER_INSERT_FAILED(1137),
    /** 订单表插入出错 */
    ORDER_FAILED(1138),
    ORDER_CANCEL_FAILED(1142),
    /** 时间参数出错 */
    TIME_PARAMETER_ERR(1139),
    /** 訂單詳情出錯 */
    ORDER_DETAIL_FAILED(1140),
    /** GMO支付失败 */
    PAYMENT_GMO_FAIL(1170),
    /** 状态错误 */
   STATUE_ERR(1141),
    /** 优惠券出错 */
    COUPON_ERR(1148),
    /** ADDRESS超出范围 */
    ADDRESS_FAILED(1149),

    /** 达到最大限制 */
    REACH_LIMIT(1200),


    // 第三方API使用
    /** 参数错误 */
    PARAMETER_ERROR(7001),

    /** 订单状态错误 */
    ORDER_STATE_ERROR(7011),

    /** 发送时间错误 */
    SEND_TIME_FORMAT(7021),

    /** 重复的数据发送 */
    REPEATED_SESSION(7031),

    /** 不存在的订单号 */
    ORDER_NO_NOT_FOUND(7041),

    /** 签名不匹配 */
    SIGNATURE_DOES_NOT_MATCH(7061),

    /** 未知错误 */
    UNKNOWN(9999),
    ;

    private final Integer state;

    ResponseCode(Integer state) {
        this.state = state;
    }

    public static Optional<ResponseCode> find(Integer code){
        Objects.requireNonNull(code);
        return Arrays.stream(ResponseCode.values()).filter(entity-> entity.is(code)).findFirst();
    }

    @Override
    public Integer getMapping() {
        return this.state;
    }
}
