package com.everyonewaiter.common.file.converter

import com.everyonewaiter.common.file.SimpleMultipartFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.MediaType
import org.springframework.util.ResourceUtils

class PdfFileConverterTest :
    FunSpec({
        context("firstPageToImage") {
            val pdfFileConverter = PdfFileConverter()
            val file = ResourceUtils.getFile("classpath:common/file/one-page.pdf")
            val pdfFile = SimpleMultipartFile(
                name = "pdfFile",
                originalFilename = file.name,
                contentType = MediaType.APPLICATION_PDF_VALUE,
                content = file.readBytes(),
            )

            test("PDF 파일의 첫 페이지를 이미지로 변환한다.") {
                ImageFormat.entries.forEach {
                    val actual = pdfFileConverter.firstPageToImage(pdfFile, "prefix", it)
                    actual.isSuccess shouldBe true
                    actual.getOrThrow().contentType shouldBe it.contentType
                }
            }

            test("파일의 형식이 PDF가 아닌 경우 예외를 발생시킨다.") {
                val invalidFile = SimpleMultipartFile(
                    name = "invalidFile",
                    originalFilename = "temp.txt",
                    contentType = MediaType.TEXT_PLAIN_VALUE,
                    content = ByteArray(0),
                )
                val actual = pdfFileConverter.firstPageToImage(invalidFile)
                actual.isFailure shouldBe true
                actual.exceptionOrNull() shouldBe IllegalStateException("PDF 파일만 이미지로 변환할 수 있습니다.")
            }
        }
    })
