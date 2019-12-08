package com.tl.commerce.config;

import com.tl.commerce.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class IntercepterConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //微信回调接口不设置拦截器token验证
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/order/**").excludePathPatterns("/order/weChat/callback");


        WebMvcConfigurer.super.addInterceptors(registry);
    }



}
