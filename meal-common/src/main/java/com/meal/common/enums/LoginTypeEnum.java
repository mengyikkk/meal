package com.meal.common.enums;

import com.meal.common.StateMapping;

public enum LoginTypeEnum implements StateMapping<String> {
    NORMAL("1"),

    MOBILE("2");

    private final String mapping;

    LoginTypeEnum(String mapping) {
        this.mapping = mapping;
    }

    @Override
    public String getMapping() {
        return this.mapping;
    }
}
