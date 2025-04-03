package com.everyonewaiter.application.image.service

import com.everyonewaiter.application.image.dto.ImageUpload
import com.everyonewaiter.common.file.converter.ImageFormatConverter
import com.everyonewaiter.common.file.converter.PdfFileConverter
import com.everyonewaiter.common.file.extension.isImage
import com.everyonewaiter.common.file.extension.isPdf
import com.everyonewaiter.domain.image.entity.Image
import com.everyonewaiter.domain.image.repository.ImageRepository
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.infrastructure.image.ImageClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val imageClient: ImageClient,
    private val imageRepository: ImageRepository,
    private val imageFormatConverter: ImageFormatConverter,
    private val pdfFileConverter: PdfFileConverter,
) {
    @Transactional
    fun upload(request: ImageUpload.Request): Long {
        val imageFile = convertToWebp(request.prefix, request.file)
        imageClient.upload(imageFile)
        val image = Image.create(request.accountId, imageFile.name, imageFile.originalFilename ?: imageFile.name)
        return imageRepository.save(image).id
    }

    private fun convertToWebp(
        prefix: String,
        file: MultipartFile,
    ): MultipartFile =
        when {
            file.isPdf ->
                pdfFileConverter.firstPageToImage(file).getOrNull()
                    ?: throw BusinessException(ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE)

            file.isImage ->
                imageFormatConverter.convertToWebp(file, prefix).getOrNull()
                    ?: throw BusinessException(ErrorCode.FAILED_CONVERT_IMAGE_FORMAT)

            else -> throw BusinessException(ErrorCode.ALLOW_IMAGE_AND_PDF_FILE)
        }
}
