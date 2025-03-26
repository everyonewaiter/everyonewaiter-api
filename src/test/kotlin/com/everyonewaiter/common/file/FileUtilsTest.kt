package com.everyonewaiter.common.file

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FileUtilsTest :
    FunSpec({
        context("generateTsidFileName") {
            test("접두사가 있는 경우 접두사를 포함한 TSID 파일 이름을 생성한다.") {
                val prefix = "image"
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
                val fileName = generateTsidFileName(prefix)
                fileName shouldStartWith "$prefix-$formattedDate-"
            }

            test("접두사가 없는 경우 접두사를 포함하지 않은 TSID 파일 이름을 생성한다.") {
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
                val fileName = generateTsidFileName()
                fileName shouldStartWith "$formattedDate-"
            }
        }

        context("directory") {
            val directoryPath = "temp"
            val directory = File(directoryPath)

            test("폴더를 생성하고 삭제한다.") {
                mkdir(directoryPath)
                directory.exists() shouldBe true
                rmdir(directoryPath)
                directory.exists() shouldBe false
            }
        }
    })
