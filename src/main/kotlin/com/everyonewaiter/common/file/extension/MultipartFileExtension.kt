package com.everyonewaiter.common.file.extension

import org.springframework.http.MediaType
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

/**
 * [MultipartFile]이 PDF 파일인지 확인합니다.
 *
 * @receiver [MultipartFile]
 * @return 파일의 Content-Type이 application/pdf인 경우 true, 그렇지 않은 경우 false
 */
val MultipartFile.isPdf: Boolean
    get() = contentType == MediaType.APPLICATION_PDF_VALUE

/**
 * [MultipartFile]이 이미지 파일인지 확인합니다.
 *
 * @receiver [MultipartFile]
 * @return 파일의 Content-Type이 image/로 시작하는 경우 true, 그렇지 않은 경우 false
 */
val MultipartFile.isImage: Boolean
    get() = contentType?.startsWith("image/") ?: false

/**
 * [MultipartFile]의 originalFilename 을 이용해 확장자명을 추출합니다.
 *
 * @receiver [MultipartFile]
 * @return 파일의 확장자
 * @throws [IllegalStateException] 파일명이 null 또는 공백이거나, 파일명에 확장자가 없는 경우
 */
val MultipartFile.extensionOrThrow: String
    get() = StringUtils
        .getFilenameExtension(originalFilename)
        ?.ifBlank { null }
        ?: throw IllegalStateException("파일명 ${originalFilename}에서 확장자를 찾을 수 없습니다.")

/**
 * [MultipartFile]의 originalFilename 을 이용해 확장자명이 있는지 확인합니다.
 *
 * @receiver [MultipartFile]
 * @return 확장자가 있는 경우 true, 없는 경우 false
 */
val MultipartFile.hasExtension: Boolean
    get() = StringUtils.getFilenameExtension(originalFilename)?.isNotBlank() ?: false
