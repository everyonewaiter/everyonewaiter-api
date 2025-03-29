package com.everyonewaiter.global.config

import com.everyonewaiter.global.annotation.ApiErrorResponse
import com.everyonewaiter.global.annotation.ApiErrorResponses
import com.everyonewaiter.global.exception.ErrorResponse
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OperationCustomizer
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
            .addOperationCustomizer(customSingleErrorResponse())
            .addOperationCustomizer(customMultipleErrorResponse())
            .addOpenApiCustomizer {
                it.addSecurityItem(jwtSecurityRequirement)
            }.packagesToScan("com.everyonewaiter.presentation.owner")
            .build()

    @Bean
    fun deviceApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("2. Store")
            .addOperationCustomizer(customSingleErrorResponse())
            .addOperationCustomizer(customMultipleErrorResponse())
            .addOpenApiCustomizer {
                it.addSecurityItem(signatureSecurityRequirement)
            }.packagesToScan("com.everyonewaiter.presentation.store")
            .build()

    @Bean
    fun adminApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("3. Admin")
            .addOperationCustomizer(customSingleErrorResponse())
            .addOperationCustomizer(customMultipleErrorResponse())
            .addOpenApiCustomizer {
                it.addSecurityItem(jwtSecurityRequirement)
            }.packagesToScan("com.everyonewaiter.presentation.admin")
            .build()

    @Bean
    fun customSingleErrorResponse(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->
            val singleErrorAnnotation = handlerMethod.getMethodAnnotation(ApiErrorResponse::class.java)
            if (singleErrorAnnotation != null) {
                val statusCode = singleErrorAnnotation.code.status.value()
                val example = Example().apply {
                    value = ErrorResponse(singleErrorAnnotation.code, singleErrorAnnotation.exampleMessage)
                }
                val swaggerMediaType = MediaType().apply {
                    addExamples(singleErrorAnnotation.exampleName, example)
                }
                val apiResponse = ApiResponse().apply {
                    description = singleErrorAnnotation.summary
                    content = Content().apply {
                        addMediaType("application/json;charset=UTF-8", swaggerMediaType)
                    }
                }
                operation.responses.addApiResponse(statusCode.toString(), apiResponse)
            }
            operation
        }

    @Bean
    fun customMultipleErrorResponse(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->
            val multipleErrorAnnotation = handlerMethod.getMethodAnnotation(ApiErrorResponses::class.java)
            multipleErrorAnnotation
                ?.value
                ?.groupBy { it.code.status.value() }
                ?.entries
                ?.associate { (statusCode, errorAnnotations) ->
                    statusCode.toString() to errorAnnotations.associate {
                        it.exampleName to Example().apply {
                            value = ErrorResponse(it.code, it.exampleMessage)
                        }
                    }
                }?.forEach { (statusCode, examples) ->
                    val swaggerMediaType =
                        MediaType().apply { examples.entries.forEach { addExamples(it.key, it.value) } }
                    val apiResponse = ApiResponse().apply {
                        description = multipleErrorAnnotation.summary
                        content = Content().apply {
                            addMediaType("application/json;charset=UTF-8", swaggerMediaType)
                        }
                    }
                    operation.responses.addApiResponse(statusCode, apiResponse)
                }
            operation
        }

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
