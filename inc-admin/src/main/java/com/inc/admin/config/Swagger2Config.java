package com.inc.admin.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置类
 */
@Configuration
//@EnableWebMvc
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {

    @Bean
    public Docket apiConfig() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("token").description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());  //根据每个方法名也知道当前方法在设置什么参数
        //创建基于Swagger2的配置文件
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                // 调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
                .apiInfo(apiInfo())
                .select()
                // 这里采用包含注解的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .apis(RequestHandlerSelectors.basePackage("com.inc.admin"))
                // 这里采用包扫描的方式来确定要显示的接口
//                .apis(any())
                .paths(PathSelectors.any())  //其它写法：.paths(regex("^.*(?<!error)$"))
                .build()
                .globalOperationParameters(pars)
                //增加Authorization请求头
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("在线接口")
                //描述
                .description("接口API文档")
                //将“url”换成自己的ip:port
                .termsOfServiceUrl("http://localhost:8001/swagger-ui.html")
                //版本
                .version("1.0")
                //作者信息
//                .contact(new Contact("iter", "http://localhost:8001", "xxx@sina.com"))
                .build();
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("^(?!auth).*$"));  // "/.*"
        return result;
    }

    //通过pathRegex获取SecurityContext对象
    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    //默认为全局的SecurityReference对象
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global",
                "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }

    private List<ApiKey> securitySchemes() {
        //设置请求头信息
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
//        ApiKey apiKey = new ApiKey("x-auth-token", "x-auth-token", "header");
        result.add(apiKey);
        return result;
    }
/*
    废弃，已实现了WebMvcConfigurer 或者 用注解@EnableWebMvc修饰过，不需要此方法。
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
*/

}
