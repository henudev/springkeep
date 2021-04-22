package com.h3c.bigdata.zhgx.conf;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@PropertySource(value = "classpath:zhgx_swagger.properties", ignoreResourceNotFound = true)
public class SwaggerConfiguration implements WebMvcConfigurer {


    @Bean
    public Docket createRestApi(@Value("${server.swagger.context-path}") String contextPath,
                                @Value("${swagger.base.package}") String basePackage, final ApiInfo apiInfo) {
        List<Parameter> params = new ArrayList<Parameter>();

        params.add(new ParameterBuilder().parameterType("header").name("Authorization")
                .modelRef(new ModelRef("string")).required(false).build());
        return new Docket(DocumentationType.SWAGGER_2).pathMapping(contextPath).apiInfo(apiInfo)
                .globalOperationParameters(params).select().apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
    }

    @Bean
    public ApiInfo apiInfo(@Value("${swagger.api.info.title}") String title,
                           @Value("${swagger.api.info.description}") String description,
                           @Value("${swagger.api.info.contact}") String contact,
                           @Value("${swagger.api.info.version}") String version) {
        return new ApiInfoBuilder().title(title).description(description).contact(contact).version(version).build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
