package com.meal.common.mapper;

import com.meal.common.dto.MealSearchHistory;
import com.meal.common.dto.MealSearchHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MealSearchHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    long countByExample(MealSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int deleteByExample(MealSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int insert(MealSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int insertSelective(MealSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    MealSearchHistory selectOneByExample(MealSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    MealSearchHistory selectOneByExampleSelective(@Param("example") MealSearchHistoryExample example, @Param("selective") MealSearchHistory.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    List<MealSearchHistory> selectByExampleSelective(@Param("example") MealSearchHistoryExample example, @Param("selective") MealSearchHistory.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    List<MealSearchHistory> selectByExample(MealSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    MealSearchHistory selectByPrimaryKeySelective(@Param("id") Long id, @Param("selective") MealSearchHistory.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    MealSearchHistory selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MealSearchHistory record, @Param("example") MealSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MealSearchHistory record, @Param("example") MealSearchHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MealSearchHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table meal_search_history
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MealSearchHistory record);
}