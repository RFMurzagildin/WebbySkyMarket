package com.example.webbyskymarket.config;

import com.example.webbyskymarket.interceptors.UserRoleInterceptor;
import com.example.webbyskymarket.interceptors.UserStatusInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserStatusInterceptor userStatusInterceptor;
    private final UserRoleInterceptor userRoleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userStatusInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/registration");
        registry.addInterceptor(userRoleInterceptor)
                .addPathPatterns("/admin/**", "/api/admin/**");
    }
}