package com.meal.wx.api.task;

import com.meal.common.dto.MealOrderExample;
import com.meal.common.mapper.MealOrderMapper;
import com.meal.wx.api.util.OrderStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
public class SchedulerTask {
    private final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    @Resource
    private MealOrderMapper mealOrderMapper;

    @Scheduled(fixedDelayString = "300000")
    public void cancelTimeoutOrders(){
        var example = new MealOrderExample();
        MealOrderExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(Boolean.FALSE)
                .andOrderStatusEqualTo(OrderStatusEnum.UNPAID.getMapping())
                .andAddTimeLessThanOrEqualTo(LocalDateTime.now().minusDays(2));

    }
}
