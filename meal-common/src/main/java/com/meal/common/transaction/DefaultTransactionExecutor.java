package com.meal.common.transaction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class DefaultTransactionExecutor implements TransactionExecutor {

    @Override
    public boolean transaction(Function<Void, Integer> transaction) {
        return this.transaction(Collections.singletonList(transaction));
    }

    @Override
    public boolean transaction(List<Function<Void, Integer>> functions) {
        return this.transaction(functions, functions.size());
    }

    @Override
    public boolean transaction(List<Function<Void,Integer>> functions,
                               Integer expectedValue) {
        return this.transactionThen(functions, expectedValue, Function.identity()) > 0;
    }

    @Override
    public <T> T transactionThen(Function<Void,Integer> function,
                                 Function<Integer, T> result){
        return this.transactionThen(Collections.singletonList(function), 1, result);
    }

    @Override
    public <T> T transactionThen(List<Function<Void,Integer>> functions,
                                 Integer expectedValue,
                                 Function<Integer, T> result){
        Integer count = functions
                .stream()
                .map(function -> function.apply(null))
                .reduce(0, Integer::sum);
        if(count.compareTo(expectedValue) != 0){
            throw new RuntimeException();
        }
        // 此处抛异常可影响事务回滚
        return result.apply(count);
    }

    @Override
    public void transactionPromise(List<Function<Void, Optional<? extends RuntimeException>>> functions) {
        for (Function<Void, Optional<? extends RuntimeException>> function : functions) {
            Optional<? extends RuntimeException> records = function.apply(null);
            if(records.isPresent()) {
                throw records.get();
            }
        }
    }
}
