package com.meal.common.mapper;

import com.meal.common.dto.MealLittleCalamity;
import com.meal.common.dto.MealLittleCalamityExample;
import java.util.List;

import com.meal.common.model.WxLittleCalamityVo;
import org.apache.ibatis.annotations.Param;

public interface MealLittleCalamityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    long countByExample(MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int deleteByExample(MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int insert(MealLittleCalamity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int insertSelective(MealLittleCalamity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    MealLittleCalamity selectOneByExample(MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    MealLittleCalamity selectOneByExampleSelective(@Param("example") MealLittleCalamityExample example, @Param("selective") MealLittleCalamity.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    MealLittleCalamity selectOneByExampleWithBLOBs(MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    List<MealLittleCalamity> selectByExampleSelective(@Param("example") MealLittleCalamityExample example, @Param("selective") MealLittleCalamity.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    List<MealLittleCalamity> selectByExampleWithBLOBs(MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    List<MealLittleCalamity> selectByExample(MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    MealLittleCalamity selectByPrimaryKeySelective(@Param("id") Long id, @Param("selective") MealLittleCalamity.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    MealLittleCalamity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    MealLittleCalamity selectByPrimaryKeyWithLogicalDelete(@Param("id") Long id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MealLittleCalamity record, @Param("example") MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") MealLittleCalamity record, @Param("example") MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MealLittleCalamity record, @Param("example") MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MealLittleCalamity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(MealLittleCalamity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MealLittleCalamity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") MealLittleCalamityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_little_calamity
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Long id);

    List<MealLittleCalamity> findMany(@Param("request") WxLittleCalamityVo request, @Param("limit") Integer limit, @Param("offset") Long offset);
}
