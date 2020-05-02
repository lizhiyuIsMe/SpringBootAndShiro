package com.example.demo.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    /**
     * 在springboot2.0.0之后继承WebMvcConfigurationSupport类，重写addInterceptors方法
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        /**
         * 拦截器按照顺序执行,如果不同拦截器拦截存在相同的URL，前面的拦截器会执行，后面的拦截器将不执行
         */
        registry.addInterceptor( new LogInterceptor())
                .addPathPatterns("/**");


        super.addInterceptors(registry);
    }
}
