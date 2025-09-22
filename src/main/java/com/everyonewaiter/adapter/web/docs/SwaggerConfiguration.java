package com.everyonewaiter.adapter.web.docs;

import com.everyonewaiter.adapter.web.api.ErrorResponse;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

@Configuration
@RequiredArgsConstructor
class SwaggerConfiguration {

  private static final String ACCESS_KEY_HEADER = "x-ew-access-key";
  private static final String SIGNATURE_HEADER = "x-ew-signature";
  private static final String TIMESTAMP_HEADER = "x-ew-timestamp";

  private final BuildProperties buildProperties;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(info())
        .servers(servers())
        .components(
            new Components()
                .addSecuritySchemes(HttpHeaders.AUTHORIZATION, bearerSecurityScheme())
                .addSecuritySchemes(ACCESS_KEY_HEADER, accessKeySecurityScheme())
                .addSecuritySchemes(SIGNATURE_HEADER, signatureSecurityScheme())
                .addSecuritySchemes(TIMESTAMP_HEADER, timestampSecurityScheme())
        );
  }

  @Bean
  public GroupedOpenApi common() {
    return GroupedOpenApi.builder()
        .group("0. Common")
        .addOperationCustomizer(errorResponse())
        .addOperationCustomizer(errorResponses())
        .addOpenApiCustomizer(
            customizer -> customizer.addSecurityItem(signatureSecurityRequirement())
        )
        .packagesToScan("com.everyonewaiter.adapter.web.api.common")
        .build();
  }

  @Bean
  public GroupedOpenApi owner() {
    return GroupedOpenApi.builder()
        .group("1. Owner")
        .addOperationCustomizer(errorResponse())
        .addOperationCustomizer(errorResponses())
        .addOpenApiCustomizer(customizer -> customizer.addSecurityItem(jwtSecurityRequirement()))
        .packagesToScan("com.everyonewaiter.adapter.web.api.owner")
        .build();
  }

  @Bean
  public GroupedOpenApi device() {
    return GroupedOpenApi.builder()
        .group("2. Device")
        .addOperationCustomizer(errorResponse())
        .addOperationCustomizer(errorResponses())
        .addOpenApiCustomizer(customizer ->
            customizer.addSecurityItem(signatureSecurityRequirement())
        )
        .packagesToScan("com.everyonewaiter.adapter.web.api.device")
        .build();
  }

  @Bean
  public GroupedOpenApi admin() {
    return GroupedOpenApi.builder()
        .group("3. Admin")
        .addOperationCustomizer(errorResponse())
        .addOperationCustomizer(errorResponses())
        .addOpenApiCustomizer(customizer -> customizer.addSecurityItem(jwtSecurityRequirement()))
        .packagesToScan("com.everyonewaiter.adapter.web.api.admin")
        .build();
  }

  @Bean
  public OperationCustomizer errorResponse() {
    return (operation, handlerMethod) -> {
      ApiErrorResponse annotation = handlerMethod.getMethodAnnotation(ApiErrorResponse.class);

      if (annotation != null) {
        ErrorCode errorCode = annotation.code();

        Example example = new Example().value(ErrorResponse.from(errorCode));
        MediaType mediaType = new MediaType().addExamples(annotation.exampleName(), example);
        Content content = new Content().addMediaType("application/json;charset=UTF-8", mediaType);
        ApiResponse response = new ApiResponse().description(annotation.summary()).content(content);

        operation.getResponses().addApiResponse(errorCode.getStatus().toString(), response);
      }

      return operation;
    };
  }

  @Bean
  public OperationCustomizer errorResponses() {
    return (operation, handlerMethod) -> {
      ApiErrorResponses annotation = handlerMethod.getMethodAnnotation(ApiErrorResponses.class);

      if (annotation != null) {
        Map<String, List<ExampleHolder>> errorGroups = new HashMap<>();

        for (ApiErrorResponse response : annotation.value()) {
          ErrorCode errorCode = response.code();
          String statusCode = String.valueOf(errorCode.getStatus().value());

          errorGroups.computeIfAbsent(statusCode, k -> new ArrayList<>())
              .add(new ExampleHolder(response.summary(), response.exampleName(), errorCode));
        }

        errorGroups.forEach((statusCode, exampleHolders) -> {
          String summary = annotation.summary();
          MediaType mediaType = new MediaType();

          for (ExampleHolder exampleHolder : exampleHolders) {
            if (StringUtils.hasText(exampleHolder.summary)) {
              summary = exampleHolder.summary;
            }

            mediaType.addExamples(exampleHolder.exampleName, exampleHolder.example);
          }

          Content content = new Content().addMediaType("application/json;charset=UTF-8", mediaType);
          ApiResponse response = new ApiResponse().description(summary).content(content);

          operation.getResponses().addApiResponse(statusCode, response);
        });
      }

      return operation;
    };
  }

  private Info info() {
    return new Info()
        .title("모두의 웨이터 API 명세서")
        .version(buildProperties.getVersion());
  }

  private List<Server> servers() {
    return List.of(
        new Server().url("http://localhost:8081").description("Local")
    );
  }

  private SecurityRequirement jwtSecurityRequirement() {
    return new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION);
  }

  private SecurityRequirement signatureSecurityRequirement() {
    return new SecurityRequirement()
        .addList(ACCESS_KEY_HEADER)
        .addList(SIGNATURE_HEADER)
        .addList(TIMESTAMP_HEADER);
  }

  private SecurityScheme bearerSecurityScheme() {
    return new SecurityScheme()
        .name(HttpHeaders.AUTHORIZATION)
        .scheme("bearer")
        .bearerFormat("JWT")
        .type(SecurityScheme.Type.HTTP)
        .in(SecurityScheme.In.HEADER);
  }

  private SecurityScheme accessKeySecurityScheme() {
    return new SecurityScheme()
        .name(ACCESS_KEY_HEADER)
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER);
  }

  private SecurityScheme signatureSecurityScheme() {
    return new SecurityScheme()
        .name(SIGNATURE_HEADER)
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER);
  }

  private SecurityScheme timestampSecurityScheme() {
    return new SecurityScheme()
        .name(TIMESTAMP_HEADER)
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER);
  }

  private record ExampleHolder(String summary, String exampleName, Example example) {

    public ExampleHolder(String summary, String exampleName, ErrorCode example) {
      this(summary, exampleName, new Example().value(ErrorResponse.from(example)));
    }

  }

}
