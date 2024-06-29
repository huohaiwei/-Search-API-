package com.yellow.usermanager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自定义 Swagger 接口文档的配置
 * @author 陈翰垒
 */
@Configuration
@EnableSwagger2WebMvc
@Profile("dev")
public class SwaggerConfig {

    @Bean(value = "dockerBean")
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //控制器位置
                .apis(RequestHandlerSelectors.basePackage("com.yellow.usermanager.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api 信息
     *
     * @return api构造器
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("API开放平台")
                .description("yellowAi用户中心接口文档")
                .termsOfServiceUrl("https://github.com/huohaiwei")
                .contact(new Contact("yellowchen","https://github.com/huohaiwei","qq1441871979@126.com"))
                .version("1.0")
                .build();
    }
}
