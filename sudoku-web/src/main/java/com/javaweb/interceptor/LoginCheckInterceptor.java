package com.javaweb.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.pojo.Result;
import com.javaweb.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        log.info("url: {}", url);

        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            log.info("token is empty,用戶未登入");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = objectMapper.writeValueAsString(error);
            response.getWriter().write(notLogin);
            return false;
        }
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("token 解析失敗,用戶未登入");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = objectMapper.writeValueAsString(error);
            response.getWriter().write(notLogin);
            return false;
        }
        log.info("token 解析成功,用戶已登入");
        return true;
    }
}
