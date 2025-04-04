package com.everyonewaiter.infrastructure.image

import com.everyonewaiter.common.file.SimpleMultipartFile
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.oracle.bmc.objectstorage.ObjectStorageClient
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class OracleObjectStorageClientTest :
    FunSpec({
        val objectStorageClient = mockk<ObjectStorageClient>(relaxed = true)
        val oracleObjectStorageClient = OracleObjectStorageClient(
            namespace = "namespace",
            bucketName = "bucketName",
            objectStorageClient = objectStorageClient,
        )

        context("upload") {
            test("이미지 업로드에 성공하면 파일명을 반환한다.") {
                val name = "license/202501/test.webp"
                val file = SimpleMultipartFile("abc.jpg", ByteArray(0), name, "image/webp")
                val actual = oracleObjectStorageClient.upload(file)
                actual shouldBe file.originalFilename
            }

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
