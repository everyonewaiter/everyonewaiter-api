package com.everyonewaiter.common.file.extension

import com.everyonewaiter.common.file.SimpleMultipartFile
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.MediaType

class MultipartFileExtensionTest :
    FunSpec({
        val pdfFile = SimpleMultipartFile(
            name = "pdfFile",
            originalFilename = "temp.pdf",
            contentType = MediaType.APPLICATION_PDF_VALUE,
            content = ByteArray(0),
        )
        val pngFile = SimpleMultipartFile(
            name = "pngFile",
            originalFilename = "temp.png",
            contentType = MediaType.IMAGE_PNG_VALUE,
            content = ByteArray(0),
        )
        val noExtensionFile = SimpleMultipartFile(
            name = "noExtensionFile",
            originalFilename = "temp.",
            content = ByteArray(0),
        )
        val unknownFile = SimpleMultipartFile(
            name = "unknownFile",
            content = ByteArray(0),
        )

        context("isPdf") {
            test("PDF 파일인지 확인한다.") {
                pdfFile.isPdf shouldBe true
                pngFile.isPdf shouldBe false
                unknownFile.isPdf shouldBe false
            }
        }

        context("isImage") {
            test("이미지 파일인지 확인한다.") {
                pdfFile.isImage shouldBe false
                pngFile.isImage shouldBe true
                unknownFile.isImage shouldBe false
            }
        }

        context("extensionOrThrow") {
            test("파일의 확장자를 추출한다.") {
                pdfFile.extensionOrThrow shouldBe "pdf"
                pngFile.extensionOrThrow shouldBe "png"
            }

            test("파일의 확장자가 없는 경우 예외를 발생시킨다.") {
                listOf(noExtensionFile, unknownFile).forEach {
                    shouldThrow<IllegalStateException> {
                        it.extensionOrThrow
                    }.message shouldBe "파일명 '${it.originalFilename}'에서 확장자를 찾지 못했어요. 파일명을 확인해 주세요."
                }
            }
        }

        context("hasExtension") {
            test("파일에 확장자가 있는지 확인한다.") {
                pngFile.hasExtension shouldBe true
                noExtensionFile.hasExtension shouldBe false
                unknownFile.hasExtension shouldBe false
            }
        }
    })
