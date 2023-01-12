package com.meal.common;

import java.util.Arrays;
import java.util.Optional;

public enum CommonState implements StateMapping<Integer> {

    OPEN(0),

    CLOSE(1)
    ;

    private final Integer state;

    CommonState(Integer state) {
        this.state = state;
    }

    public static Optional<CommonState> find(Integer integer){
        return Arrays.stream(CommonState.values()).filter(entity->entity.is(integer)).findFirst();
    }

    public static CommonState convert(boolean express){
        return express ? CommonState.OPEN : CommonState.CLOSE;
    }

    public boolean isOpen(){
        return this == OPEN;
    }

    public boolean isClose(){
        return this == CLOSE;
    }

    @Override
    public Integer getMapping() {
        return this.state;
    }
}
