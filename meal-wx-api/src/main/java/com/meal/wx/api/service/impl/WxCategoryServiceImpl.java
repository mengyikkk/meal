package com.meal.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealCategory;
import com.meal.common.dto.MealCategoryExample;
import com.meal.common.mapper.MealCategoryMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.WxCategoryVo;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WxCategoryServiceImpl implements WxCategoryService {
    @Resource
    private MealCategoryMapper mealCategoryMapper;
    @Resource
    private MealShopMapper mealShopMapper;

    private final Logger logger = LoggerFactory.getLogger(WxGoodsServiceImp.class);

    @Override
    public Result<?> list(Long uid, Long shopId) {
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Product[list][block]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var example = new MealCategoryExample();
        example.createCriteria().andLogicalDeleted(Boolean.TRUE).andShopIdIn(List.of(0L, shopId));
        var categoryList = this.mealCategoryMapper.selectByExample(example).stream().map(e -> {
            var vo = new WxCategoryVo();
            vo.setId(e.getId());
            vo.setName(e.getName());
            vo.setParentId(e.getPid().longValue());
            return vo;
        }).collect(Collectors.toList());
        var sonMap = categoryList.stream().filter(e -> 0L != e.getParentId()).collect(Collectors.groupingBy(WxCategoryVo::getParentId));
        categoryList.forEach(e -> e.setCategoryVos(sonMap.get(e.getId())));
        return ResultUtils.success(categoryList.stream().filter(m -> 0L == m.getParentId()).collect(Collectors.toList()));
    }
}
