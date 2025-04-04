package com.everyonewaiter.common.file.converter

import com.everyonewaiter.common.file.SimpleMultipartFile
import com.everyonewaiter.common.file.extension.isPdf
import com.everyonewaiter.common.file.generateTsidFileName
import com.sksamuel.scrimage.ImmutableImage
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

/**
 * PDF 파일을 이미지 또는 다른 파일 형식으로 변환하는 클래스입니다.
 */
@Component
class PdfFileConverter {
    /**
     * PDF 파일의 첫 페이지를 이미지로 변환합니다.
     * 이미지의 이름은 랜덤으로 생성되며 형식은 `{prefix}/{yyyyMM}/{TSID}.{format}`입니다.
     * 접두사를 지정하지 않으면 이미지의 이름은 `{yyyyMM}/{TSID}.{format}` 형식으로 생성됩니다.
     *
     * @param [file] 변환할 PDF 파일
     * @param [prefix] 변환된 이미지 파일의 이름에 붙일 접두사
     * @param [format] 이미지 포맷 DEFAULT: [ImageFormat.WEBP]
     * @return 변환된 이미지 파일
     * @throws [IllegalStateException] 파일이 PDF 형식이 아닌 경우
     * @throws [IOException] PDF 파일을 읽거나 이미지로 변환하는 중 오류가 발생한 경우
     * @see [com.everyonewaiter.common.tsid.Tsid.nextString]
     * @see [com.everyonewaiter.common.file.generateTsidFileName]
     */
    fun firstPageToImage(
        file: MultipartFile,
        prefix: String? = null,
        format: ImageFormat = ImageFormat.WEBP,
    ): Result<MultipartFile> =
        runCatching {
            check(file.isPdf) { "PDF 파일만 이미지로 변환할 수 있어요. PDF 파일을 업로드 해주세요." }
            PDDocument.load(file.inputStream).use { document ->
                val name = file.originalFilename ?: "firstPageToImage"
                val originalFileName = "${generateTsidFileName(prefix)}.${format.extension}"
                val bufferedImage = PDFRenderer(document).renderImageWithDPI(0, 300F, ImageType.RGB)
                val content = ImmutableImage.fromAwt(bufferedImage).bytes(format.writer)
                SimpleMultipartFile(name, content, originalFileName, format.contentType)
            }
        }
}
