package com.meal.common.transaction;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface TransactionExecutor {

    /**
     * 运行单条事务语句，并检查其是否更改了1条记录。如果预期结果不同，则事务会回滚，并抛出异常
     * @param transaction 通常是持久化操作的动作
     * @return {@code true} 符合预期值, {@code false}不符合预期值
     * @throws RuntimeException 如果预期值不符合
     */
    @Transactional
    boolean transaction(Function<Void, Integer> transaction);

    /**
     * 运行事务语句，并检查其是否与列表的大小相同。如果两者不相同，则事务会回滚，并抛出异常
     *
     * @param functions 通常是持久化操作的动作列表
     * @return {@code true} 符合预期值, {@code false}不符合预期值
     * @throws RuntimeException 如果预期值不符合
     */
    @Transactional
    boolean transaction(List<Function<Void, Integer>> functions);

    /**
     * 运行事务语句，并检查其是否与预期的值相同。如果两者不相同，则事务会回滚，并抛出异常
     *
     * @param functions 通常是持久化操作的动作列表
     * @param expectedValue 预期会被更改的值
     * @return {@code true} 符合预期值, {@code false}不符合预期值
     * @throws RuntimeException 如果预期值不符合
     */
    @Transactional
    boolean transaction(List<Function<Void,Integer>> functions,
                        Integer expectedValue);

    /**
     * 运行事务语句，并检查其是否只更改了一条记录。如果两者不相同，则事务会回滚，并抛出异常
     * 允许在预期检查通过后，运行一部份逻辑，并且在其中引发的运行时异常被认为可以回滚事务
     *
     * @param function 通常是持久化操作的动作
     * @param result 预期结果符合后，将执行此操作
     * @param <T> result操作中希望返回的结果类型
     * @return result操作中返回的结果
     * @throws RuntimeException 如果预期值不符合
     */
    @Transactional
    <T> T transactionThen(Function<Void,Integer> function,
                          Function<Integer, T> result);

    /**
     * 运行事务语句，并检查其是否与预期的值相同。如果两者不相同，则事务会回滚，并抛出异常
     * 允许在预期检查通过后，运行一部份逻辑，并且在其中引发的运行时异常被认为可以回滚事务
     *
     * @param functions 通常是持久化操作的动作列表
     * @param expectedValue 预期会被更改的值
     * @param result 预期结果符合后，将执行此操作
     * @param <T> result操作中希望返回的结果类型
     * @return result操作中返回的结果
     * @throws RuntimeException 如果预期值不符合
     */
    @Transactional
    <T> T transactionThen(List<Function<Void,Integer>> functions,
                          Integer expectedValue,
                          Function<Integer, T> result);

    /**
     * 运行事务，该操作是中断型的。即，当某个事务返回异常时，后续语句不再执行。而直接抛出返回的异常。
     * 因此列表中的事务语句的先后顺序具有一定的考量
     * 直观感觉下，将失败率更高的语句放在较前方会在性能上拥有更好的效果
     * @param functions 通常是持久化操作的动作列表
     * @throws RuntimeException 如果任何一个动作返回了异常
     */
    @Transactional
    void transactionPromise(List<Function<Void, Optional<? extends RuntimeException>>> functions) throws RuntimeException;
}
