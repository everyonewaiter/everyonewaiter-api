package com.everyonewaiter.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

private const val ACCESS_KEY_HEADER = "x-ew-access-key"
private const val SIGNATURE_HEADER = "x-ew-signature"
private const val TIMESTAMP_HEADER = "x-ew-timestamp"

@Configuration
class SwaggerConfiguration(
    private val buildProperties: BuildProperties,
) {
    @Bean
    fun openApi(): OpenAPI =
        OpenAPI()
            .info(info)
            .servers(servers)
            .components(
                Components()
                    .addSecuritySchemes(HttpHeaders.AUTHORIZATION, userSecurityScheme)
                    .addSecuritySchemes(ACCESS_KEY_HEADER, accessKeySecurityScheme)
                    .addSecuritySchemes(SIGNATURE_HEADER, signatureSecurityScheme)
                    .addSecuritySchemes(TIMESTAMP_HEADER, timestampSecurityScheme),
            )

    @Bean
    fun userApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("1. Owner")
            .addOpenApiCustomizer {
                it.addSecurityItem(jwtSecurityRequirement)
            }.packagesToScan("com.everyonewaiter.presentation.owner")
            .build()

    @Bean
    fun deviceApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("2. Store")
            .addOpenApiCustomizer {
                it.addSecurityItem(signatureSecurityRequirement)
            }.packagesToScan("com.everyonewaiter.presentation.store")
            .build()

    @Bean
    fun adminApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("3. Admin")
            .addOpenApiCustomizer {
                it.addSecurityItem(jwtSecurityRequirement)
            }.packagesToScan("com.everyonewaiter.presentation.admin")
            .build()

    private val info: Info
        get() =
            Info().apply {
                title = "모두의 웨이터 API 명세서"
                version = buildProperties.version
            }

    private val servers: List<Server>
        get() = listOf(
            Server().apply { url = "https://everyonewaiter.com" },
            Server().apply { url = "http://localhost:8080" },
        )

    private val jwtSecurityRequirement: SecurityRequirement
        get() =
            SecurityRequirement().apply {
                addList(HttpHeaders.AUTHORIZATION)
            }

    private val signatureSecurityRequirement: SecurityRequirement
        get() =
            SecurityRequirement().apply {
                addList(ACCESS_KEY_HEADER)
                addList(SIGNATURE_HEADER)
                addList(TIMESTAMP_HEADER)
            }

    private val userSecurityScheme: SecurityScheme
        get() = SecurityScheme()
            .name(HttpHeaders.AUTHORIZATION)
            .scheme("bearer")
            .bearerFormat("JWT")
            .type(SecurityScheme.Type.HTTP)
            .`in`(SecurityScheme.In.HEADER)

    private val accessKeySecurityScheme: SecurityScheme
        get() = SecurityScheme()
            .name(ACCESS_KEY_HEADER)
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)

    private val signatureSecurityScheme: SecurityScheme
        get() = SecurityScheme()
            .name(SIGNATURE_HEADER)
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)

    private val timestampSecurityScheme: SecurityScheme
        get() = SecurityScheme()
            .name(TIMESTAMP_HEADER)
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
}
