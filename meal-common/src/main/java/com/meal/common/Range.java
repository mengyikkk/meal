package com.meal.common;

import java.io.Serializable;

public interface Range <T extends Serializable & Comparable<? super T>>{

    T begin();

    T end();
}
