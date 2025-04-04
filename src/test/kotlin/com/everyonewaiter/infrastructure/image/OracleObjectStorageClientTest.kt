package com.everyonewaiter.infrastructure.image

import com.everyonewaiter.common.file.SimpleMultipartFile
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OracleObjectStorageClientTest :
    FunSpec({
        val oracleObjectStorageClient = OracleObjectStorageClient(
            namespace = "namespace",
            bucketName = "bucketName",
        )

        context("upload") {
            test("파일명을 찾을 수 없다면 예외가 발생한다.") {
                val file = SimpleMultipartFile("", ByteArray(0))
                shouldThrow<BusinessException> {
                    oracleObjectStorageClient.upload(file)
                }.errorCode shouldBe ErrorCode.NOT_FOUND_FILENAME
            }

            test("파일에 확장자가 없다면 예외가 발생한다.") {
                val file = SimpleMultipartFile("", ByteArray(0), "test")
                shouldThrow<BusinessException> {
                    oracleObjectStorageClient.upload(file)
                }.errorCode shouldBe ErrorCode.NOT_FOUND_EXTENSION
            }

            test("파일의 Content-Type을 찾을 수 없다면 예외가 발생한다.") {
                val file = SimpleMultipartFile("", ByteArray(0), "test.webp")
                shouldThrow<BusinessException> {
                    oracleObjectStorageClient.upload(file)
                }.errorCode shouldBe ErrorCode.NOT_FOUND_CONTENT_TYPE
            }
        }
    })
