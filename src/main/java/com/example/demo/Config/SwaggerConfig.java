package com.example.demo.Config;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${system.deploy.domain}")
    private String deployDomain;

    private ApiInfo metaData() {
        return new ApiInfoBuilder().title("Spring Boot Demo Project").description("")
                .version("1.0.0").license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("Louis Nguyen", "https://it4u.top", "tho.n@intelizest.com")).build();
    }

    private List<ResponseMessage> globalResponses() {
        final List<ResponseMessage> globalResponses = Arrays.asList(
                new ResponseMessageBuilder().code(200).message("Successfully retrieved data").build(),
                new ResponseMessageBuilder().code(401).message("You are not authorized to view the resource").build(),
                new ResponseMessageBuilder().code(403)
                        .message("Accessing the resource you were trying to reach is forbidden").build(),
                new ResponseMessageBuilder().code(404).message("The resource you were trying to reach is not found")
                        .build(),
                new ResponseMessageBuilder().code(500).message("Internal Error").build());

        return globalResponses;
    }

    @Bean
    public Docket allApis() {
        final List<ResponseMessage> responseMessages = globalResponses();
        return new Docket(DocumentationType.SWAGGER_2)
                .host(this.deployDomain)
                .useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages).apiInfo(metaData()).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.Controller")).build()
                .securitySchemes(Lists.newArrayList(apiKey())).securityContexts(Lists.newArrayList(securityContext()));
    }

    @Bean
    SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}