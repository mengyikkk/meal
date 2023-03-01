package com.meal.common.utils;


import com.meal.common.ResponseCode;
import com.meal.common.Result;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ResultUtils {
    private ResultUtils() {}
    private static class SimpleResult<E> implements Result<E> {
        private final ResponseCode code;
        private final String description;
        private final E result;
        private final Long records;

        public SimpleResult(ResponseCode code,
                            String description,
                            E result,
                            Long records) {
            this.code = code;
            this.description = description;
            this.result = result;
            this.records = records;
        }

        @Override
        public ResponseCode code() {
            return this.code;
        }

        @Override
        public boolean success() {
            return this.code == ResponseCode.SUCCESS;
        }

        @Override
        public String description() {
            return this.description;
        }

        @Override
        public E result() {
            return this.result;
        }

        @Override
        public Long records() {
            return this.records;
        }

        public Integer getCode() {
            return code.getMapping();
        }

        public String getDescription() {
            return description;
        }

        public E getResult() {
            return result;
        }

        public Long getRecords() {
            return records;
        }
    }

    public static class ResultBuilder<E> {
        private ResponseCode code;
        private String description;
        private E result;
        private Long records;

        private ResultBuilder() {}

        public Result<E> build(){
            Objects.requireNonNull(this.code);
            return new SimpleResult<>(
                    this.code,
                    this.description,
                    this.result,
                    this.records);
        }

        public ResponseCode getCode() {
            return code;
        }

        public ResultBuilder<E> setCode(ResponseCode code) {
            this.code = code;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public ResultBuilder<E> setDescription(String description) {
            this.description = description;
            return this;
        }

        public E getResult() {
            return result;
        }

        public ResultBuilder<E> setResult(E result) {
            this.result = result;
            return this;
        }

        public Long getRecords() {
            return records;
        }

        public ResultBuilder<E> setRecords(Long records) {
            this.records = records;
            return this;
        }
    }

    public static<E> Result<List<E>> successEmpty() {
        return successWithEntities(Collections.emptyList(), 0L);
    }

    public static<E> Result<List<E>> successWithEntities(List<E> result, Long records) {
        return entities(ResponseCode.SUCCESS, result, records);
    }

    public static<E> Result<E> success() {
        return success(null);
    }

    public static<E> Result<E> success(E result) {
        return entity(ResponseCode.SUCCESS, result);
    }

    public static<E> Result<E> unknown() {
        return code(ResponseCode.UNKNOWN);
    }

    public static<E> Result<E> message(ResponseCode code, String message) {
        ResultBuilder<E> builder = createBuilder();
        return builder.setDescription(message).setCode(code).build();
    }

    public static<E> Result<E> entityMessage(ResponseCode code, String message ,E result) {
        ResultBuilder<E> builder = createBuilder();
        return builder.setDescription(message).setResult(result).setCode(code).build();
    }

    public static<E> Result<E> entity(ResponseCode code, E result) {
        ResultBuilder<E> builder = createBuilder();
        return builder.setResult(result).setCode(code).build();
    }

    public static<E> Result<E> entities(ResponseCode code, E result, Long records) {
        ResultBuilder<E> builder = createBuilder();
        return builder.setResult(result).setCode(code).setRecords(records).build();
    }

    public static<E> Result<E> code(ResponseCode code) {
        ResultBuilder<E> builder = createBuilder();
        return builder.setCode(code).build();
    }

    private static<E> ResultBuilder<E> createBuilder() {
        return new ResultBuilder<>();
    }

}

