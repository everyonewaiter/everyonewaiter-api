package com.everyonewaiter.application.image.service

import com.everyonewaiter.application.image.dto.ImageUpload
import com.everyonewaiter.common.file.converter.ImageFormatConverter
import com.everyonewaiter.common.file.converter.PdfFileConverter
import com.everyonewaiter.common.file.extension.isImage
import com.everyonewaiter.common.file.extension.isPdf
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.infrastructure.image.ImageClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val imageClient: ImageClient,
    private val imageFormatConverter: ImageFormatConverter,
    private val pdfFileConverter: PdfFileConverter,
) {
    fun upload(request: ImageUpload.Request): String {
        val file = convertToWebp(request.file, request.prefix)
        return imageClient.upload(file)
    }

    private fun convertToWebp(
        file: MultipartFile,
        prefix: String?,
    ): MultipartFile =
        when {
            file.isPdf -> {
                val image = pdfFileConverter.firstPageToImage(file, prefix).getOrNull()
                image ?: throw BusinessException(ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE)
            }

            file.isImage -> {
                val image = imageFormatConverter.convertToWebp(file, prefix).getOrNull()
                image ?: throw BusinessException(ErrorCode.FAILED_CONVERT_IMAGE_FORMAT)
            }

            else -> throw BusinessException(ErrorCode.ALLOW_IMAGE_AND_PDF_FILE)
        }
}
