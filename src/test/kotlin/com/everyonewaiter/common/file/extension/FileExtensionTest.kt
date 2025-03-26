package com.everyonewaiter.common.file.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.File

class FileExtensionTest :
    FunSpec({
        val file = File("temp.txt")
        val multipleDotFile = File("temp.abc.def.png")
        val noExtensionFile = File("temp.")
        val emptyFile = File("")

        context("extensionOrThrow") {
            test("파일의 확장자를 추출한다.") {
                file.extensionOrThrow shouldBe "txt"
                multipleDotFile.extensionOrThrow shouldBe "png"
            }

            test("파일의 확장자가 없는 경우 예외를 발생시킨다.") {
                listOf(noExtensionFile, emptyFile).forEach {
                    shouldThrow<IllegalStateException> {
                        it.extensionOrThrow
                    }.message shouldBe "파일명 ${it.name}에서 확장자를 찾을 수 없습니다."
                }
            }
        }

        context("hasExtension") {
            test("파일에 확장자가 있는지 확인한다.") {
                file.hasExtension shouldBe true
                noExtensionFile.hasExtension shouldBe false
            }
        }

        context("deleteWithLog") {
            test("파일을 삭제한다.") {
                val target = File("soon-delete.txt")
                target.createNewFile()
                target.exists() shouldBe true
                target.deleteWithLog()
                target.exists() shouldBe false
            }

            test("폴더를 삭제한다.") {
                val directory = File("soon-delete")
                directory.mkdir()
                directory.exists() shouldBe true
                directory.deleteWithLog()
                directory.exists() shouldBe false
            }
        }
    })
