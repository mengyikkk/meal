package com.meal.common.mapper;

import com.meal.common.dto.MealOrderGoods;
import com.meal.common.dto.MealOrderGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MealOrderGoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    long countByExample(MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int deleteByExample(MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int insert(MealOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int insertSelective(MealOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    MealOrderGoods selectOneByExample(MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    MealOrderGoods selectOneByExampleSelective(@Param("example") MealOrderGoodsExample example, @Param("selective") MealOrderGoods.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    List<MealOrderGoods> selectByExampleSelective(@Param("example") MealOrderGoodsExample example, @Param("selective") MealOrderGoods.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    List<MealOrderGoods> selectByExample(MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    MealOrderGoods selectByPrimaryKeySelective(@Param("id") Long id, @Param("selective") MealOrderGoods.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    MealOrderGoods selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    MealOrderGoods selectByPrimaryKeyWithLogicalDelete(@Param("id") Long id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MealOrderGoods record, @Param("example") MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MealOrderGoods record, @Param("example") MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MealOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MealOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") MealOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_order_goods
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Long id);

    int batchInsert(List<MealOrderGoods> items);
}
