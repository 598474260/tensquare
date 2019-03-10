package com.tensquare.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 配置类，配置Security框架，请求允许匿名访问
 */
@Configuration          // 声明是配置类
@EnableWebSecurity      // Security框架开关
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 请求路径允许匿名访问
     * /** 子孙目录文件
     * /* 子目录文件
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable();
    }

}
