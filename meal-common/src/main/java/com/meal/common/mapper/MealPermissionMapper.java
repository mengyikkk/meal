package com.meal.common.mapper;

import com.meal.common.dto.MealPermission;
import com.meal.common.dto.MealPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MealPermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    long countByExample(MealPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int deleteByExample(MealPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int insert(MealPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int insertSelective(MealPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    MealPermission selectOneByExample(MealPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    MealPermission selectOneByExampleSelective(@Param("example") MealPermissionExample example, @Param("selective") MealPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    List<MealPermission> selectByExampleSelective(@Param("example") MealPermissionExample example, @Param("selective") MealPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    List<MealPermission> selectByExample(MealPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    MealPermission selectByPrimaryKeySelective(@Param("id") Long id, @Param("selective") MealPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    MealPermission selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MealPermission record, @Param("example") MealPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MealPermission record, @Param("example") MealPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MealPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MealPermission record);
}