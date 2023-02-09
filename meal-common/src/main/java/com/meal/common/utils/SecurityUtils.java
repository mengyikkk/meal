package com.meal.common.utils;

import com.meal.common.dto.MealUser;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    public SecurityUtils() {
    }

    public static MealUser getUser() {
        MealUser user = (MealUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword(null);
        user.setUsername(user.getUsername());
        return user;
    }
    /**
     * 在security中获取用户名
     * @return
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 在security中获取用户ID
     *
     * @return
     */
    public static Long getUserId() {
        return getUser().getId();
    }

    /**
     * 在security中获取用户小程序ID
     * @return
     */
    public static String getOpenId() {
        return getUser().getWxOpenid();
    }
}
