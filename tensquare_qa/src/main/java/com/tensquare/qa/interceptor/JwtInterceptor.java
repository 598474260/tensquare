package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * controller方法执行前，拦截
     * 解析token，存入到request域对象中
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器拦截了...");

        // 获取请求头
        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader---------" +authHeader);
        // 判断是否为空
        if (authHeader != null) {
            String token = authHeader.substring(7);
            System.out.println("token---------:"+token);
            if (token != null) {
                // 解析token
                Claims claims = jwtUtil.parseToken(token);
                if (claims != null) {
                    // 获取角色信息
                    String roles = (String) claims.get("roles");
                    System.out.println(roles);
                    // 保存
                    if ("admin".equals(roles)) {
                        request.setAttribute("admin_claims", claims);
                    }
                    if ("user".equals(roles)) {
                        request.setAttribute("user_claims", claims);
                    }
                }
            }
        }
        // 放行
        return true;
    }
}
