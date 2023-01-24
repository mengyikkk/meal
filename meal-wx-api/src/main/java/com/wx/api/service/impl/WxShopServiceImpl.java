package com.wx.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.meal.common.Result;
import com.meal.common.dto.MealShopExample;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.utils.ResultUtils;
import com.wx.api.service.WxShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxShopServiceImpl implements WxShopService {
    @Resource
    private MealShopMapper mealShopMapper;

    @Override
    public Result<?> shop(Integer page, Integer limit) {
        var example = new MealShopExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("sort_order"+" "+"asc");
        PageHelper.startPage(page,limit);
        return ResultUtils.success(this.mealShopMapper.selectByExample(example));
    }
}
