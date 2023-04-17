package com.meal.wx.api.task;

import com.meal.common.dto.MealOrder;
import com.meal.common.dto.MealOrderExample;
import com.meal.common.mapper.MealOrderMapper;
import com.meal.common.utils.JsonUtils;
import com.meal.wx.api.util.OrderStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .andAddTimeLessThanOrEqualTo(LocalDateTime.now().minusHours(2));
        List<MealOrder> mealOrders = this.mealOrderMapper.selectByExample(example);
        this.logger.info("这些订单准备取消:{}", JsonUtils.toJson(mealOrders.stream().map(MealOrder::getId).collect(Collectors.toList())));
        if (!ObjectUtils.isEmpty(mealOrders)){
            MealOrder mealOrder = new MealOrder();
            mealOrder.setEndTime(LocalDateTime.now());
            mealOrder.setOrderStatus(OrderStatusEnum.CANCEL.getMapping());
            this.mealOrderMapper.updateByExampleSelective(mealOrder,example);
        }
    }
}
