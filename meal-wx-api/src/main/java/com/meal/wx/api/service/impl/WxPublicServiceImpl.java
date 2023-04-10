package com.meal.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealOrder;
import com.meal.common.dto.MealOrderExample;
import com.meal.common.dto.MealUser;
import com.meal.common.dto.MealUserExample;
import com.meal.common.mapper.MealOrderMapper;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxPublicVo;
import com.meal.common.utils.ResultUtils;
import com.meal.wx.api.service.WxPublicService;
import com.meal.wx.api.util.OrderStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WxPublicServiceImpl implements WxPublicService {
    @Resource
    private Validator validator;
    private final Logger logger = LoggerFactory.getLogger(WxPublicServiceImpl.class);

    @Resource
    private MealOrderMapper mealOrderMapper;

    @Resource
    private MealUserMapper mealUserMapper;

    @Override
    public Result<?> ship(WxPublicVo vo) {
        {
            Set<ConstraintViolation<WxPublicVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Good[goodsList][block]:param.  request: {}, violation: {}", vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var example = new MealOrderExample();
        example.createCriteria().andShopIdEqualTo(vo.getShopId())
                .andOrderStatusEqualTo(OrderStatusEnum.PAID.getMapping())
                .andIsTimeOnSaleEqualTo(vo.getIsTimeOnSale())
                .andShipTimeBetween(vo.getShipDate().atStartOfDay().minusSeconds(1),
                        vo.getShipDate().plusDays(1).atStartOfDay());
        var userMap =
                this.mealOrderMapper.selectByExample(example).stream().collect(Collectors.groupingBy(MealOrder::getUserId));
        var exampleUser = new MealUserExample();
        exampleUser.createCriteria().andIdIn(new ArrayList<>(userMap.keySet()));
        List<MealUser> mealUsers = this.mealUserMapper.selectByExample(exampleUser);

        return  ResultUtils.unknown();
    }
}
