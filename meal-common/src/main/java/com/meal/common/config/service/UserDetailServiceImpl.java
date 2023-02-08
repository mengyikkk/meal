package com.meal.common.config.service;


import com.meal.common.dto.MealMenu;
import com.meal.common.dto.MealUser;
import com.meal.common.mapper.MealUserMapper;
import com.meal.common.service.MealUserService;
import com.meal.common.utils.JsonUtils;
import com.meal.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实现UserDetailsService接口，实现自定义登陆逻辑
 * 重写loadUserByUsername方法
 *
 * @author ajie
 * @createTime 2021年07月30日 22:23:00
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private MealUserService mealUserService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private  MealUserMapper mealUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        //判断缓存中是否存在用户信息 存在则直接从缓存中取，不存在则查询数据库并把数据存入缓存
        MealUser user;
        if (redisUtils.haskey("userInfo_" + username)) {
            //缓存中存在用户信息，直接从redis中取
            user =(MealUser) redisUtils.getValue("userInfo_" + username);
            redisUtils.expire("userInfo_" + username, 5);
        } else {
            user = mealUserService.queryByMobile(username);
            if (null == user) {
                throw new UsernameNotFoundException("用户名或密码错误！");
            }
            if (user.getAdmin()) {
                //非管理员需要查询角色信息
                user.setRoles(mealUserMapper.findRoles(null));
                user.setPermissions(mealUserMapper.findPermissions(null));
                //获取父级菜单
                List<MealMenu> menus = mealUserMapper.findMenus(null);
                //获取子级菜单
                menus.forEach(item -> item.setChildren(mealUserMapper.findChildrenMenu(item.getId(), null)));
                user.setMenus(menus);
            } else {
                //非管理员需要查询角色信息
                user.setRoles(mealUserMapper.findRoles(user.getId()));
                user.setPermissions(mealUserMapper.findPermissions(user.getId()));
                //获取父级菜单
                List<MealMenu> menus = mealUserMapper.findMenus(user.getId());
                //获取子级菜单
                menus.forEach(item -> item.setChildren(mealUserMapper.findChildrenMenu(item.getId(), user.getId())));
                user.setMenus(menus);
            }
            redisUtils.setValueTime("userInfo_" + username, user, 5);
        }
        return user;
    }
}
