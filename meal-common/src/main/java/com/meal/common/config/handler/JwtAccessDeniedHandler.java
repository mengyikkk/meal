package com.meal.common.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meal.common.ResponseCode;
import com.meal.common.utils.ResultUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 没有权限访问时返回结果
 * @createTime 2021年07月27日 22:23:00
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(403);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(ResultUtils.message(ResponseCode.TOKEN_ILLEGAL,"权限不足，请联系管理员！")));
        writer.flush();
        writer.close();
    }
}
