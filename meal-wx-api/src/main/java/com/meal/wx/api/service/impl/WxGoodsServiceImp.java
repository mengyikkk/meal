package com.meal.wx.api.service.impl;

import com.meal.common.ResponseCode;
import com.meal.common.Result;
import com.meal.common.dto.MealGoods;
import com.meal.common.dto.MealGoodsExample;
import com.meal.common.dto.MealLittleCalamity;
import com.meal.common.dto.MealLittleCalamityExample;
import com.meal.common.mapper.MealGoodsMapper;
import com.meal.common.mapper.MealLittleCalamityMapper;
import com.meal.common.mapper.MealShopMapper;
import com.meal.common.model.WxGoodsResponseVo;
import com.meal.common.model.WxGoodsVo;
import com.meal.common.model.WxLittleCalamityResponseVo;
import com.meal.common.model.WxLittleCalamityVo;
import com.meal.common.utils.BeanCopyUtils;
import com.meal.common.utils.ResultUtils;
import com.meal.common.utils.SecurityUtils;
import com.meal.wx.api.service.WxGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WxGoodsServiceImp implements WxGoodsService {
    @Resource
    private Validator validator;
    private final Logger logger = LoggerFactory.getLogger(WxGoodsServiceImp.class);

    @Resource
    private MealShopMapper mealShopMapper;
    @Resource
    private MealGoodsMapper mealGoodsMapper;

    @Resource
    private MealLittleCalamityMapper mealLittleCalamityMapper;

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
        this.process(criteria, vo);
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
        return ResultUtils.successWithEntities(convert(list), count);
    }

    private List<WxGoodsResponseVo> convert(List<MealGoods> goods){
        return  goods.stream().map(e-> new WxGoodsResponseVo().setGoodsId(e.getId())
                .setGoodsSn(e.getGoodsSn()).setGoodsName(e.getName())
                                .setAddTime(e.getAddTime()).setIsOnSale(e.getIsOnSale()).setIsTimeOnSale(e.getIsTimeOnSale()).setPicUrl(e.getPicUrl()).setSortOrder(e.getSortOrder())
                                .setUnit(e.getUnit()).setPrice(e.getRetailPrice()).setDetail(e
                                        .getDetail())
                )
                .collect(Collectors.toList());
    }
    @Override
    public Result<?> littleCalamityList(WxLittleCalamityVo vo) {
        {
            Set<ConstraintViolation<WxLittleCalamityVo>> violations = this.validator.validate(vo);
            if (!violations.isEmpty()) {
                this.logger.warn("Service-Good[littleCalamityList][block]:param.  request: {}, violation: {}",
                        vo, violations);
                return ResultUtils.code(ResponseCode.PARAMETER_ERROR);
            }
        }
        var shop = this.mealShopMapper.selectByPrimaryKeyWithLogicalDelete(vo.getShopId(), Boolean.FALSE);
        if (Objects.isNull(shop)) {
            this.logger.warn("Service-Product[littleCalamityList][block]:shop. uid: {}, request: {}",
                    SecurityUtils.getUserId(), vo);
            return ResultUtils.message(ResponseCode.SHOP_FIND_ERR0, "店铺不可用");
        }
        if (Objects.nonNull(vo.getGoodsId())){
            var good =this.mealGoodsMapper.selectByPrimaryKeyWithLogicalDelete(vo.getGoodsId(),Boolean.FALSE);
            if (Objects.isNull(good)){
                this.logger.warn("Service-Product[littleCalamityList][block]:good. uid: {}, request: {}",
                        SecurityUtils.getUserId(), vo);
                return ResultUtils.message(ResponseCode.GOOD_FIND_ERR0, "");
            }
        }
        var example = new MealLittleCalamityExample();
        var criteria = example.createCriteria();
        criteria.andDeletedEqualTo(Boolean.FALSE);
        criteria.andShopIdIn(List.of(shop.getId(), 0L));
        this.littleCalamityProcess(criteria, vo);
        var count = this.mealLittleCalamityMapper.countByExample(example);
        if (count == 0) {
            return ResultUtils.successWithEntities(null, count);
        }
        long offset = 0;
        Integer limit = null;
        if ((Objects.nonNull(vo.getPage()) || Objects.nonNull(vo.getLimit()))) {
            limit = vo.getLimit();
            offset = ((long) (vo.getPage() - 1) * limit);
        }
        var list = this.mealLittleCalamityMapper.findMany(vo, limit, offset);
        return ResultUtils.successWithEntities(this.convertCalamities(list), count);
    }
    private List<WxLittleCalamityResponseVo> convertCalamities(List<MealLittleCalamity> goods){
        return  goods.stream().map(e-> new WxLittleCalamityResponseVo().setGoodsId(e.getGoodsId())
                        .setCalamityId(e.getId()).setCalamityName(e.getName())
                        .setShopId(e.getShopId()).setIsOnSale(e.getIsOnSale()).setIsTimeOnSale(e.getIsTimeOnSale())
                        .setPicUrl(e.getPicUrl())
                        .setUnit(e.getUnit()).setPrice(e.getRetailPrice()).setBrief(e.getBrief())
                )
                .collect(Collectors.toList());
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
    private void littleCalamityProcess(MealLittleCalamityExample.Criteria criteria, WxLittleCalamityVo vo) {
        if (Objects.nonNull(vo.getGoodsId())) {
            criteria.andGoodsIdIn(List.of(0L,vo.getGoodsId()));
        }
        if (Objects.nonNull(vo.getCalamityId())){
            criteria.andIdEqualTo(vo.getCalamityId());
        }
        if (Objects.nonNull(vo.getCalamityName())){
            criteria.andNameLike("%"+vo.getCalamityName()+"%");
        }
        if (Objects.nonNull(vo.getIsOnSale())) {
            criteria.andIsOnSaleEqualTo(vo.getIsOnSale());
        }
        if (Objects.nonNull(vo.getIsTimeOnSale())) {
            criteria.andIsTimeOnSaleEqualTo(vo.getIsTimeOnSale());
        }
    }
}
