package com.everyonewaiter.common.file.extension

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

/**
 * [File]의 name 을 이용해 확장자명을 추출합니다.
 *
 * @receiver [File]
 * @return 파일의 확장자
 * @throws [IllegalStateException] 파일명에 확장자가 없는 경우
 */
val File.extensionOrThrow: String
    get() = extension.ifBlank { throw IllegalStateException("파일명 '$name'에서 확장자를 찾지 못했어요. 파일명을 확인해 주세요.") }

/**
 * [File]의 name 을 이용해 확장자명이 있는지 확인합니다.
 *
 * @receiver [File]
 * @return 확장자가 있는 경우 true, 없는 경우 false
 */
val File.hasExtension: Boolean
    get() = extension.isNotBlank()

/**
 * 파일 또는 폴더의 삭제 성공, 실패 로그와 함께 [File]을 삭제합니다.
 */
fun File.deleteWithLog() {
    val resource = if (isDirectory) "폴더" else "파일"
    if (exists()) {
        if (delete()) {
            logger.info { "[$resource] 삭제: $absolutePath" }
        } else {
            logger.warn { "[$resource] 삭제 실패: $absolutePath" }
        }
    } else {
        logger.info { "[$resource 삭제] 찾을 수 없음 : $absolutePath" }
    }
}
