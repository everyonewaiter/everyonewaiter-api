package com.everyonewaiter.common.file.converter

import com.sksamuel.scrimage.nio.ImageWriter
import com.sksamuel.scrimage.nio.JpegWriter
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.webp.WebpWriter
import org.springframework.http.MediaType

/**
 * 이미지 포맷을 나타내는 열거형 클래스입니다.
 *
 * @property [extension] 이미지 포맷의 확장자
 * @property [contentType] 이미지 포맷의 Content-Type
 * @property [writer] 이미지 포맷을 쓰기 위한 ImageWriter
 * @see [com.sksamuel.scrimage.nio.ImageWriter]
 */
enum class ImageFormat(
    val extension: String,
    val contentType: String,
    val writer: ImageWriter,
) {
    PNG("png", MediaType.IMAGE_PNG_VALUE, PngWriter.NoCompression),
    JPG("jpg", MediaType.IMAGE_JPEG_VALUE, JpegWriter.Default),
    WEBP("webp", "image/webp", WebpWriter.DEFAULT),
}
