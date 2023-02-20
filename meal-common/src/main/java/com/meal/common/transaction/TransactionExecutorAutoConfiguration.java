package com.meal.common.transaction;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;

@Configuration
@AutoConfigureAfter(TransactionAutoConfiguration.class)
@ConditionalOnBean(TransactionManager.class)
public class TransactionExecutorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TransactionExecutor transactionExecutor(){
        return new DefaultTransactionExecutor();
    }
}
