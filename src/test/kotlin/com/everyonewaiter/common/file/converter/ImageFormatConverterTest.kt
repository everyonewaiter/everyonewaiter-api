package com.everyonewaiter.common.file.converter

import com.everyonewaiter.common.file.SimpleMultipartFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.springframework.http.MediaType
import org.springframework.util.ResourceUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ImageFormatConverterTest :
    FunSpec({
        context("convertToWebp") {
            val imageFormatConverter = ImageFormatConverter()
            val file = ResourceUtils.getFile("classpath:common/file/icon-image.png")
            val imageFile = SimpleMultipartFile(
                name = "imageFile",
                originalFilename = file.name,
                contentType = MediaType.IMAGE_PNG_VALUE,
                content = file.readBytes(),
            )
            val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))

            test("접두사 포함 이미지의 포맷을 WebP로 변환한다.") {
                val prefix = "image"
                val actual = imageFormatConverter.convertToWebp(imageFile, prefix)
                actual.isSuccess shouldBe true
                actual.getOrThrow().contentType shouldBe ImageFormat.WEBP.contentType
                actual.getOrThrow().originalFilename shouldStartWith "$prefix-$formattedDate-"
            }

            test("접두사 없이 이미지의 포맷을 WebP로 변환한다.") {
                val actual = imageFormatConverter.convertToWebp(imageFile)
                actual.isSuccess shouldBe true
                actual.getOrThrow().contentType shouldBe ImageFormat.WEBP.contentType
                actual.getOrThrow().originalFilename shouldStartWith "$formattedDate-"
            }
        }
    })
