package com.wx.api.service.impl;

import com.meal.common.Result;
import com.meal.common.dto.MealShopExample;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.ShopResponseVo;
import com.meal.common.utils.BeanCopyUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.model.ShopRequestVo;
import com.wx.api.service.WxShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class WxShopServiceImpl implements WxShopService {
    @Resource
    private MealShopMapper mealShopMapper;

    @Override
    public Result<?> shop(ShopRequestVo request) {
        var example = new MealShopExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        var count=this.mealShopMapper.countByExample(example);
        if (count==0){
            return ResultUtils.success();
        }
        long offset = 0;
        Integer limit = null;
        if (Objects.nonNull(request)&&(Objects.nonNull(request.getPage())||Objects.nonNull(request.getLimit()))){
            limit = request.getLimit();
            offset =   ((long) (request.getPage() - 1) * limit);
        }
        var shopList= mealShopMapper.findMany(request, limit, offset);
        return ResultUtils.successWithEntities(BeanCopyUtils.copyBeanList(shopList,ShopResponseVo.class),count);
    }

}
