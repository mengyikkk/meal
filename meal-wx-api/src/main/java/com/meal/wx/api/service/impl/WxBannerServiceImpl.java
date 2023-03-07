package com.meal.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealBanner;
import com.meal.common.dto.MealBannerExample;
import com.meal.common.mapper.MealBannerMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.BannerVo;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class WxBannerServiceImpl implements WxBannerService {
    @Resource
    private MealShopMapper mealShopMapper;

    @Resource
    private MealBannerMapper mealBannerMapper;
    private final Logger logger = LoggerFactory.getLogger(WxBannerServiceImpl.class);

    @Override
    public Result<?> list(Long shopId) {
        if(Objects.nonNull(shopId))
        {
            var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(shopId, Boolean.FALSE);
            if (Objects.isNull(shop)) {
                this.logger.warn("Service-Banner[list][block]:store. uid: {}, request: {}", SecurityUtils.getUserId(), shopId);
                return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
            }
        }
        var example = new MealBannerExample();
        MealBannerExample.Criteria criteria = example.createCriteria();
        criteria.andLogicalDeleted(Boolean.TRUE);
        criteria.andShopIdEqualTo(0L);
        example.setOrderByClause("order_num asc");
        if (Objects.nonNull(shopId)) {
            criteria.andShopIdEqualTo(shopId);
        }
        var result = this.mealBannerMapper.selectByExample(example).stream().map(this::cover).collect(Collectors.toList());
        return ResultUtils.successWithEntities(result, (long) result.size());
    }

    private BannerVo cover(MealBanner banner) {
        BannerVo bannerVo = new BannerVo();
        bannerVo.setBannerUrl(banner.getBannerUrl());
        bannerVo.setOrderNum(banner.getOrderNum());
        bannerVo.setDescription(banner.getDesc());
        bannerVo.setName(banner.getName());
        return bannerVo;
    }
}
