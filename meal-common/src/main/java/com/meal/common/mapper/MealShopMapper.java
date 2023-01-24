package com.meal.common.mapper;

import com.meal.common.dto.MealShop;
import com.meal.common.dto.MealShopExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MealShopMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    long countByExample(MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int deleteByExample(MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int insert(MealShop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int insertSelective(MealShop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    MealShop selectOneByExample(MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    MealShop selectOneByExampleSelective(@Param("example") MealShopExample example, @Param("selective") MealShop.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    List<MealShop> selectByExampleSelective(@Param("example") MealShopExample example, @Param("selective") MealShop.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    List<MealShop> selectByExample(MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    MealShop selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") MealShop.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    MealShop selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    MealShop selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MealShop record, @Param("example") MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MealShop record, @Param("example") MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MealShop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MealShop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") MealShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_shop
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}