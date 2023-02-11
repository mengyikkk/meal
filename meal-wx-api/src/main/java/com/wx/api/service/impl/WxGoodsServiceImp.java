package com.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealGoodsExample;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.WxGoodsResponseVo;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.utils.BeanCopyUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.wx.api.service.WxGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class WxGoodsServiceImp implements WxGoodsService {
    @Resource
    private Validator validator;
    private final Logger logger = LoggerFactory.getLogger(WxGoodsServiceImp.class);

    @Resource
    private MealShopMapper mealShopMapper;
    @Resource
    private MealGoodsMapper mealGoodsMapper;

    @Override
    public Result<?> goodsList(WxGoodsVo vo) {
        {
            Set<ConstraintViolation<WxGoodsVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Good[goodsList][block]:param.  request: {}, violation: {}",
                        vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(vo.getShopId(), Boolean.FALSE);
        if (Objects.isNull(shop)) {
            this.logger.warn("Service-Product[list][block]:store. uid: {}, request: {}",
                    SecurityUtils.getUserId(), vo);
            return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
        }
        var example = new MealGoodsExample();
        var criteria = example.createCriteria();
        criteria.andDeletedEqualTo(Boolean.FALSE);
        criteria.andShopIdIn(List.of(shop.getId(), 0L));
        var count = this.mealGoodsMapper.countByExample(example);
        if (count == 0) {
            return ResultUtils.successWithEntities(null, count);
        }
        long offset = 0;
        Integer limit = null;
        if ((Objects.nonNull(vo.getPage()) || Objects.nonNull(vo.getLimit()))) {
            limit = vo.getLimit();
            offset = ((long) (vo.getPage() - 1) * limit);
        }
        var list = this.mealGoodsMapper.findMany(vo, limit, offset);
        return ResultUtils.successWithEntities(BeanCopyUtils.copyBeanList(list, WxGoodsResponseVo.class), count);
    }

    private void process(MealGoodsExample.Criteria criteria, WxGoodsVo vo) {

        if (Objects.nonNull(vo.getGoodId())) {
            criteria.andIdEqualTo(vo.getGoodId());
        }
        if (Objects.nonNull(vo.getGoodsSn())) {
            criteria.andGoodsSnEqualTo(vo.getGoodsSn());
        }
        if (Objects.nonNull(vo.getGoodsName())) {
            criteria.andNameLike("%" + vo.getGoodsName() + "%");
        }
        if (Objects.nonNull(vo.getCategoryId())) {
            criteria.andCategoryIdEqualTo(vo.getCategoryId());
        }
        if (Objects.nonNull(vo.getIsOnSale())) {
            criteria.andIsOnSaleEqualTo(vo.getIsOnSale());
        }
        if (Objects.nonNull(vo.getIsTimeOnSale())) {
            criteria.andIsTimeOnSaleEqualTo(vo.getIsTimeOnSale());
        }
    }
}
