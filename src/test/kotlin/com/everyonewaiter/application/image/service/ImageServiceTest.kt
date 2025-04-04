package com.everyonewaiter.application.image.service

import com.everyonewaiter.application.image.dto.ImageUpload
import com.everyonewaiter.common.file.SimpleMultipartFile
import com.everyonewaiter.common.file.converter.ImageFormatConverter
import com.everyonewaiter.common.file.converter.PdfFileConverter
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.infrastructure.image.ImageClient
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ImageServiceTest :
    FunSpec({
        val imageClient = mockk<ImageClient>(relaxed = true)
        val imageFormatConverter = mockk<ImageFormatConverter>()
        val pdfFileConverter = mockk<PdfFileConverter>()
        val imageService = ImageService(
            imageClient = imageClient,
            imageFormatConverter = imageFormatConverter,
            pdfFileConverter = pdfFileConverter,
        )

        afterEach { clearAllMocks() }

        context("upload") {
            val prefix = "license/202501"

            test("PDF 파일을 업로드하면 PDF 파일을 이미지로 변환하여 업로드한다.") {
                val name = "test.pdf"
                val file = SimpleMultipartFile("abc.pdf", ByteArray(0), "$prefix/$name", "application/pdf")
                every { pdfFileConverter.firstPageToImage(file, prefix) } returns Result.success(file)
                imageService.upload(ImageUpload.Request(file, prefix))
                verify { pdfFileConverter.firstPageToImage(file, prefix) }
                verify { imageClient.upload(file) }
            }

            test("PDF 파일 변환에 실패하면 예외가 발생한다.") {
                val name = "test.pdf"
                val file = SimpleMultipartFile("abc.pdf", ByteArray(0), "$prefix/$name", "application/pdf")
                every { pdfFileConverter.firstPageToImage(file, prefix) } returns Result.failure(Exception())
                shouldThrow<BusinessException> {
                    imageService.upload(ImageUpload.Request(file, prefix))
                }.errorCode shouldBe ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE
                verify { imageClient wasNot Called }
            }

            test("이미지 파일을 업로드하면 이미지 파일을 압축 및 WebP로 변환하여 업로드한다.") {
                val name = "test.webp"
                val file = SimpleMultipartFile("abc.jpg", ByteArray(0), "$prefix/$name", "image/webp")
                every { imageFormatConverter.convertToWebp(file, prefix) } returns Result.success(file)
                imageService.upload(ImageUpload.Request(file, prefix))
                verify { imageFormatConverter.convertToWebp(file, prefix) }
                verify { imageClient.upload(file) }
            }

            test("이미지 파일 압축 및 WebP 변환에 실패하면 예외가 발생한다.") {
                val name = "test.webp"
                val file = SimpleMultipartFile("abc.jpg", ByteArray(0), "$prefix/$name", "image/webp")
                every { imageFormatConverter.convertToWebp(file, prefix) } returns Result.failure(Exception())
                shouldThrow<BusinessException> {
                    imageService.upload(ImageUpload.Request(file, prefix))
                }.errorCode shouldBe ErrorCode.FAILED_CONVERT_IMAGE_FORMAT
                verify { imageClient wasNot Called }
            }

            test("파일이 PDF 또는 이미지가 아니면 예외가 발생한다.") {
                val name = "test.txt"
                val file = SimpleMultipartFile("abc.txt", ByteArray(0), "$prefix/$name", "text/plain")
                shouldThrow<BusinessException> {
                    imageService.upload(ImageUpload.Request(file, prefix))
                }.errorCode shouldBe ErrorCode.ALLOW_IMAGE_AND_PDF_FILE
                verify { imageClient wasNot Called }
            }
        }
    })
