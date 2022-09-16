package com.inc.admin.config;

import com.inc.admin.intercepter.AuthIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    @Bean
    public AuthIntercepter authIntercepter() {
        return new AuthIntercepter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(authIntercepter());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");
        addInterceptor.excludePathPatterns("/user/register**");
        addInterceptor.excludePathPatterns("/test/**");
        addInterceptor.excludePathPatterns("/files/**");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");

        //swagger 相关
        addInterceptor.excludePathPatterns("/");
        addInterceptor.excludePathPatterns("/csrf");

        addInterceptor.excludePathPatterns("/doc.html");
        addInterceptor.excludePathPatterns("/swagger-ui.html");
        addInterceptor.excludePathPatterns("/swagger/v2/api-docs/**");
        addInterceptor.excludePathPatterns("/v2/**");
        addInterceptor.excludePathPatterns("/webjars/**");
        addInterceptor.excludePathPatterns("/swagger-resources/**");
    }
}
