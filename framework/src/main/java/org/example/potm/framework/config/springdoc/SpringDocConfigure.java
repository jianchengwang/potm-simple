package org.example.potm.framework.config.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

/**
 * @author jianchengwang
 * @date 2023/3/30
 */
@Configuration
public class SpringDocConfigure {
    @Value("${spring.application.name}")
    private String applicationName;

    private String tokenName = "Authorization";

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-api")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("client-api")
                .pathsToMatch("/api/client/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
                .group("common-api")
                .pathsToMatch("/api/common/**")
                .build();
    }

    @Bean
    public GroupedOpenApi thirdpartyApi() {
        return GroupedOpenApi.builder()
                .group("thirdparty-api")
                .pathsToMatch("/api/thirdparty/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Components components = new Components();
        //添加右上角的统一安全认证
        components.addSecuritySchemes(tokenName,
                new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .scheme("basic")
                        .name(tokenName)
                        .in(SecurityScheme.In.HEADER)
                        .description("请求头"));
        OpenAPI openAPI = new OpenAPI()
                .components(components)
                .info(apiInfo())
                .security(Collections.singletonList(
                        new SecurityRequirement().addList(tokenName)));
        return openAPI;
    }

    private Info apiInfo() {
        Contact contact = new Contact();
        contact.setName("wjc");
        return new Info()
                .title(applicationName + "-服务API")
                .version("1.0")
                .contact(contact)
                .description(applicationName + "服务文档API");
    }
}

