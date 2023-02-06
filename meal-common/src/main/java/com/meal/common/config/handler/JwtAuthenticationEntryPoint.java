package com.meal.common.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meal.common.ResponseCode;
import com.meal.common.utils.ResultUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当用户未登录和toekn过期的情况下访问资源
 * @author ajie
 * @createTime 2021年07月27日 22:20:00
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(ResultUtils.message(ResponseCode.TOKEN_ILLEGAL,"权限不足，请联系管理员！")));
        writer.flush();
        writer.close();
    }
}
