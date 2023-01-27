package com.meal.common.service;

import com.meal.common.dto.MealUser;
import com.meal.common.dto.MealUserExample;
import com.meal.common.mapper.MealUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class MealUserService {
    @Resource
    private MealUserMapper mealUserMapper;

    public MealUser queryByOid(String openId) {
        MealUserExample example = new MealUserExample();
        example.createCriteria().andWxOpenidEqualTo(openId).andDeletedEqualTo(false);
        return this.mealUserMapper.selectOneByExample(example);
    }
    public MealUser queryByMobile(String mobile) {
        MealUserExample example = new MealUserExample();
        example.createCriteria().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return this.mealUserMapper.selectOneByExample(example);
    }
    public void add(MealUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        mealUserMapper.insertSelective(user);
    }

}
