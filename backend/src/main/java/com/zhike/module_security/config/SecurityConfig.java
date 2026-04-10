package com.zhike.module_security.config;

import com.zhike.module_security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置类
 * 配置Spring Security的安全规则和过滤器
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置安全过滤链
     * @param http HttpSecurity对象
     * @return 安全过滤链
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 禁用CSRF保护
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用无状态会话
            .and()
            .authorizeHttpRequests() // 配置请求授权
            .requestMatchers("/auth/**").permitAll() // 认证相关接口允许访问
            .requestMatchers("/admin/**").hasRole("ADMIN") // 管理员接口需要ADMIN角色
            .requestMatchers("/teacher/**").hasRole("TEACHER") // 教师接口需要TEACHER角色
            .requestMatchers("/student/**").hasRole("STUDENT") // 学生接口需要STUDENT角色
            .anyRequest().authenticated(); // 其他请求需要认证

        // 在UsernamePasswordAuthenticationFilter之前添加JWT认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 配置密码编码器
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}