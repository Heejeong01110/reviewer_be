package org.movie.reviewer.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  private static final String API_NAME = "Reviewer API 명세서";
  private static final String API_VERSION = "v1.0.0";
  private static final String API_DESCRIPTION = "영화 조회 및 검색 서비스 API 명세서";

  private static String ACCESS_TOKEN = "Authorization";
  private static String REFRESH_TOKEN = "Authorization-refresh";
  private static String TOKEN = "Bearer ";

  @Bean
  public OpenAPI getOpenApi() {
    return new OpenAPI()
        .info(getApiInfo())
        .addSecurityItem(getSecurityRequirement())
        .components(getComponents());
  }

  private Info getApiInfo() {
    return new Info()
        .version(API_VERSION)
        .title(API_NAME)
        .description(API_DESCRIPTION);
//        .license(new License().name(LICENSE).url(LICENSE_URL));
  }

  private SecurityRequirement getSecurityRequirement() {
    return new SecurityRequirement()
        .addList(ACCESS_TOKEN)
        .addList(REFRESH_TOKEN);
  }

  private Components getComponents() {
    return new Components()
        .addSecuritySchemes(ACCESS_TOKEN, getAccessTokenSecurityScheme())
        .addSecuritySchemes(REFRESH_TOKEN, getRefreshTokenSecurityScheme());
  }

  private SecurityScheme getAccessTokenSecurityScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .in(SecurityScheme.In.HEADER)
        .scheme("bearer")
        .bearerFormat("JWT")
        .name("AccessToken");
  }

  private SecurityScheme getRefreshTokenSecurityScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .in(SecurityScheme.In.HEADER)
        .scheme("bearer")
        .bearerFormat("JWT")
        .name("RefreshToken");
  }

  @Bean
  public GroupedOpenApi apiVersion1() {
    return GroupedOpenApi.builder()
        .group("v1-definition")
        .pathsToMatch("/api/v1/**")
        .build();
  }

}
