package com.meal.common.utils;


import com.meal.common.Range;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public final class RangeUtils {
    private RangeUtils() {}

    private static class SimpleRange <T extends Serializable & Comparable<? super T>> implements Range<T> {
        private final T begin;
        private final T end;

        public SimpleRange(T begin, T end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public T begin() {
            return this.begin;
        }

        @Override
        public T end() {
            return this.end;
        }

        public T getBegin() {
            return begin;
        }

        public T getEnd() {
            return end;
        }
    }

    public static Range<LocalDateTime> beginTime(LocalDateTime begin) {
        Objects.requireNonNull(begin);
        return new SimpleRange<LocalDateTime>(begin, null);
    }

    public static Range<LocalDateTime> endTime(LocalDateTime end) {
        Objects.requireNonNull(end);
        return new SimpleRange<LocalDateTime>(null, end);
    }

    public static Range<LocalDateTime> rangeTime(LocalDateTime begin, LocalDateTime end) {
        Objects.requireNonNull(begin);
        Objects.requireNonNull(end);
        assertTrue(begin.isBefore(end) || begin.isEqual(end),
                String.format("Can range time of:[begin: %s, end: %s]", begin, end));
        return new SimpleRange<LocalDateTime>(begin, end);
    }

    private static void assertTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
}

