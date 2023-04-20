package com.meal.wx.api;

import com.meal.common.config.MealProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.meal")
@MapperScan("com.meal.common.mapper")
@EnableTransactionManagement
@EnableScheduling
@EnableConfigurationProperties(MealProperties.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Component
class RedisConnectionChecker implements ApplicationListener<ApplicationStartedEvent> {

    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisConnectionChecker(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            redisConnectionFactory.getConnection().close();
        } catch (Exception e) {
            System.err.println("Redis connection failed, shutting down application...");
            System.exit(-1);
        }
    }
}
