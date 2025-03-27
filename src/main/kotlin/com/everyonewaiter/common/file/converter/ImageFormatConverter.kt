package com.everyonewaiter.common.file.converter

import com.everyonewaiter.common.file.SimpleMultipartFile
import com.everyonewaiter.common.file.extension.isImage
import com.everyonewaiter.common.file.generateTsidFileName
import com.sksamuel.scrimage.ImmutableImage
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

/**
 * 이미지의 포맷을 변환하는 클래스입니다.
 */
@Component
class ImageFormatConverter {
    /**
     * 이미지를 WebP 포맷으로 변환합니다.
     * 이미지의 이름은 랜덤으로 생성되며 형식은 `{prefix}-{yyyyMM}-{TSID}.webp`입니다.
     * 접두사를 지정하지 않으면 이미지의 이름은 `{yyyyMM}-{TSID}.webp` 형식으로 생성됩니다.
     *
     * @param [image] 변환할 이미지 파일
     * @param [prefix] 변환된 이미지 파일의 이름에 붙일 접두사
     * @return 변환된 이미지 파일
     * @throws [IllegalStateException] 파일이 이미지 형식이 아닌 경우
     * @throws [IOException] 이미지 포맷 변환 중 오류가 발생한 경우
     * @see [com.everyonewaiter.common.tsid.Tsid.nextString]
     * @see [com.everyonewaiter.common.file.generateTsidFileName]
     */
    fun convertToWebp(
        image: MultipartFile,
        prefix: String? = null,
    ): Result<MultipartFile> =
        runCatching {
            check(image.isImage) { "이미지 파일만 WebP 포맷으로 변환할 수 있어요. 이미지 파일을 업로드 해주세요." }
            val name = image.originalFilename ?: "convertToWebp"
            val originalFileName = "${generateTsidFileName(prefix)}.${ImageFormat.WEBP.extension}"
            val content = ImmutableImage
                .loader()
                .fromStream(image.inputStream)
                .bytes(ImageFormat.WEBP.writer)
            SimpleMultipartFile(name, content, originalFileName, ImageFormat.WEBP.contentType)
        }
}
