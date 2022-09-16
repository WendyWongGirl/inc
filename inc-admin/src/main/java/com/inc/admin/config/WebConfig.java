package com.inc.admin.config;

import com.inc.admin.intercepter.AuthIntercepter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.filePath}")
    String filePath;

    @Value("${app.pre}")
    String pre;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(pre+"/**").addResourceLocations("file:///"+filePath);

        //解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        //解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars");
        //解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        //解决bootstrap路径无法访问
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Bean
    public AuthIntercepter authenticationInterceptor() {
        //拦截器
        return new AuthIntercepter();
    }
}
