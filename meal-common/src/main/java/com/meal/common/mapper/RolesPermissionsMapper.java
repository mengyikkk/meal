package com.meal.common.mapper;

import com.meal.common.dto.RolesPermissions;
import com.meal.common.dto.RolesPermissionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RolesPermissionsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    long countByExample(RolesPermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    int deleteByExample(RolesPermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    int insert(RolesPermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    int insertSelective(RolesPermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    RolesPermissions selectOneByExample(RolesPermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    RolesPermissions selectOneByExampleSelective(@Param("example") RolesPermissionsExample example, @Param("selective") RolesPermissions.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    List<RolesPermissions> selectByExampleSelective(@Param("example") RolesPermissionsExample example, @Param("selective") RolesPermissions.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    List<RolesPermissions> selectByExample(RolesPermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") RolesPermissions record, @Param("example") RolesPermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table roles_permissions
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") RolesPermissions record, @Param("example") RolesPermissionsExample example);
}