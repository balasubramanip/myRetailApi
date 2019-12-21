package com.target.myretail.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Retail Product APIs",
                                                               "Product API to support the product details and update the details",
                                                               "1.0.0",
                                                               "",
                                                               new Contact("",
                                                                           "",
                                                                           ""),
                                                               "",
                                                               "",
                                                               new ArrayList<>());

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.target.myretail.controller"))
                .paths(regex("/retail.*"))
                .build()
                .apiInfo(DEFAULT_API_INFO);
    }
}
