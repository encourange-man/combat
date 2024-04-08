package com.chenyue.combat.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author chenyue
 * @Date 2023/7/28
 */
@Configuration
public class SwaggerConfig {

    //TODO: swagger在线html404
    @Bean
    public Docket api() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Combat 接口文档")
                .description("API Description")
                .version("1.0.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chenyue.combat"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo);

    }
}
