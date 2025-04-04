package com.everyonewaiter.application.store.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

class Apply {
    @Schema(name = "Apply.Request")
    data class Request(
        @Schema(description = "매장 이름", example = "홍길동식당")
        @field:Size(min = 1, max = 30, message = "매장 이름은 1자 이상 30자 이하로 입력해 주세요.")
        val name: String,
        @Schema(description = "대표자명", example = "홍길동")
        @field:Size(min = 1, max = 20, message = "대표자명은 1자 이상 20자 이하로 입력해 주세요.")
        val ceoName: String,
        @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123")
        @field:Size(min = 1, max = 100, message = "매장 주소는 1자 이상 100자 이하로 입력해 주세요.")
        val address: String,
        @Schema(description = "매장 전화번호", example = "02-123-4567")
        @field:Pattern(
            regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$",
            message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요.",
        )
        val landline: String,
        @Schema(description = "사업자 등록번호", example = "443-60-00875")
        @field:Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}\$", message = "잘못된 형식의 사업자 등록번호를 입력하셨어요. 사업자 등록번호를 다시 입력해 주세요.")
        val license: String,
        @Schema(description = "사업자 등록증 파일", example = "business_license.pdf")
        @field:NotNull(message = "사업자 등록증 파일(PDF 또는 이미지)을 선택해 주세요.")
        val file: MultipartFile,
    )
}
