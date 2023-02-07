package com.wx.api;

import com.meal.common.config.MealProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages="com.*")
@MapperScan("com.meal.common.mapper")
@EnableTransactionManagement
@EnableConfigurationProperties(MealProperties.class)
//@EnableConfigurationProperties(WebAppProperties.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
