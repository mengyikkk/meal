package com.meal.common;

public interface Result<E> {

    /**
     * 标识状态,用于标识失败或成功时的更进一级的描述
     * @return 标识状态
     */
    ResponseCode code();

    boolean success();

    String description();

    /**
     * 结果集
     * @return 业务上的结果
     */
    E result();

    /**
     * 总记录数, 如果需要返回的情况下
     * 通常用于向查询页面展示本次条件所匹配的总记录数，用于分页模块的渲染
     * @return 记录数
     */
    Long records();

}

