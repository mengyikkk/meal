package com.meal.wx.api.service.impl;

import com.meal.common.Result;
import com.meal.common.dto.MealShopExample;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.ShopResponseVo;
import com.meal.common.utils.BeanCopyUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.model.ShopRequestVo;
import com.meal.wx.api.service.WxShopService;
import com.meal.wx.api.util.OrderSnUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class WxShopServiceImpl implements WxShopService {
    @Resource
    private MealShopMapper mealShopMapper;
    @Override
    public Result<?> shop(ShopRequestVo request) {
        var example = new MealShopExample();
        MealShopExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(Boolean.FALSE);
        if (Objects.nonNull(request)&& Objects.nonNull(request.getShopId())){
            criteria.andIdEqualTo(request.getShopId());
        }
        if (Objects.nonNull(request)&& Objects.nonNull(request.getShopName())){
            criteria.andNameLike("%" +request.getShopName()+"%" );
        }
        var count=this.mealShopMapper.countByExample(example);
        if (count==0){
            return ResultUtils.successWithEntities(null,count);
        }
        long offset = 0;
        Integer limit = null;
        if (Objects.nonNull(request)&&(Objects.nonNull(request.getPage())||Objects.nonNull(request.getLimit()))){
            limit = request.getLimit();
            offset =   ((long) (request.getPage() - 1) * limit);
        }
        var shopList= mealShopMapper.findMany(request, limit, offset);
        List<ShopResponseVo> shopResponseVos = BeanCopyUtils.copyBeanList(shopList, ShopResponseVo.class);
        shopResponseVos.forEach(e->e.setFlag(OrderSnUtils.isBetween2200And0600()));
        return ResultUtils.successWithEntities(shopResponseVos,count);
    }

}
