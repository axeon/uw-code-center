package uw.code.center.conf;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Springdoc OpenAPI 文档配置。
 * <p>
 * 仅在 {@code debug}/{@code dev} Profile 下生效。为 {@code uw.code.center.controller.ops} 包下的接口
 * 生成 {@code opsApi} 分组文档，并注入基于 Bearer Token 的统一鉴权方案（{@code AuthToken}）。
 * </p>
 */
@Configuration
@Profile({"debug","dev"})
public class SwaggerConfig {

    /**
     * 应用名称
     */
    @Value("${project.name}")
    private String appName;

    /**
     * 应用版本
     */
    @Value("${project.version}")
    private String appVersion;

    /**
     * 注册 OpenApi 定制器：注入 Bearer Token 鉴权方案与文档基本信息（标题、版本、联系人）。
     *
     * @return OpenApiCustomizer
     */
    @Bean
    public OpenApiCustomizer customOpenAPI() {
        return openApi -> openApi
                .addSecurityItem(new SecurityRequirement().addList("AuthToken"))
                .components(openApi.getComponents().addSecuritySchemes("AuthToken",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").in(SecurityScheme.In.HEADER)))
                .info(new Info().title(appName).version(appVersion)
                        .contact(new Contact().name("axeon").email("23231269@qq.com")));
    }

    /**
     * 注册 ops 接口分组，扫描 {@code uw.code.center.controller.ops} 包。
     *
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi opsApi() {
        return GroupedOpenApi.builder()
                .group("opsApi")
                .packagesToScan("uw.code.center.controller.ops")
                .addOpenApiCustomizer(customOpenAPI())
                .build();
    }

}
