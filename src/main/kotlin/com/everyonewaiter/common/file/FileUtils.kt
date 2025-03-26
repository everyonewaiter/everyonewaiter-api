package com.everyonewaiter.common.file

import com.everyonewaiter.common.file.extension.deleteWithLog
import com.everyonewaiter.common.tsid.Tsid
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

/**
 * 파일의 이름을 TSID를 기반으로 생성합니다.
 * 생성된 파일 이름에는 확장자를 포함하고 있지 않습니다.
 * [prefix]가 있는 경우 `{prefix}-{yyyyMM}-{TSID}` 형식입니다.
 * [prefix]가 null 또는 공백인 경우 `{yyyyMM}-{TSID}` 형식입니다.
 *
 * @param [prefix] 파일 이름에 붙일 접두사
 * @return 생성된 파일 이름
 */
fun generateTsidFileName(prefix: String? = null): String {
    val formattedPrefix = if (prefix.isNullOrBlank()) "" else "$prefix-"
    val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
    return "$formattedPrefix$formattedDate-${Tsid.nextString()}"
}

/**
 * 폴더를 생성합니다.
 *
 * @param [path] 생성할 폴더의 경로
 */
fun mkdir(path: String) {
    val directory = File(path)
    val directoryPath = directory.absolutePath
    if (directory.exists()) {
        logger.info { "[폴더] 이미 생성되어 있음: $directoryPath" }
    } else {
        if (directory.mkdirs()) {
            logger.info { "[폴더] 생성: $directoryPath" }
        } else {
            logger.warn { "[폴더] 생성 실패: $directoryPath" }
        }
    }
}

/**
 * 폴더, 하위 폴더, 하위 폴더의 파일까지 전부 삭제합니다.
 *
 * @param [path] 삭제할 폴더의 경로
 */
fun rmdir(path: String) {
    val directory = File(path)
    if (directory.exists() && directory.isDirectory) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                rmdir(file.absolutePath)
            } else {
                file.deleteWithLog()
            }
        }
        directory.deleteWithLog()
    }
}
