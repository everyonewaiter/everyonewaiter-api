package com.everyonewaiter.infrastructure.image

import com.everyonewaiter.common.file.extension.deleteWithLog
import com.everyonewaiter.common.file.extension.hasExtension
import com.everyonewaiter.common.file.extension.toFile
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.global.extension.checkOrThrow
import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.Region
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.http.client.StandardClientProperties
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration
import com.oracle.bmc.objectstorage.transfer.UploadManager
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

private val logger = KotlinLogging.logger {}

@Component
class OracleObjectStorageClient(
    @Value("\${oracle.object.storage.namespace}") val namespace: String,
    @Value("\${oracle.object.storage.bucket-name}") val bucketName: String,
) : ImageClient {
    override fun upload(file: MultipartFile): String {
        checkOrThrow(file.originalFilename != null, ErrorCode.NOT_FOUND_FILENAME)
        checkOrThrow(file.hasExtension, ErrorCode.NOT_FOUND_EXTENSION)
        checkOrThrow(file.contentType != null, ErrorCode.NOT_FOUND_CONTENT_TYPE)
        val convertedFile = file.toFile
        try {
            initialize().use { client ->
                val request = UploadRequest
                    .builder(convertedFile)
                    .allowOverwrite(true)
                    .build(
                        PutObjectRequest
                            .builder()
                            .namespaceName(namespace)
                            .bucketName(bucketName)
                            .objectName(file.originalFilename)
                            .contentType(file.contentType)
                            .contentLength(convertedFile.length())
                            .cacheControl("max-age=86400")
                            .build(),
                    )
                val configuration = UploadConfiguration
                    .builder()
                    .allowMultipartUploads(true)
                    .allowParallelUploads(true)
                    .build()
                UploadManager(client, configuration).upload(request)
                return file.originalFilename!!
            }
        } catch (exception: Exception) {
            logger.error(exception) { "[이미지 업로드 실패] ${exception.message}" }
            throw BusinessException(ErrorCode.FAILED_UPLOAD_IMAGE)
        } finally {
            convertedFile.deleteWithLog()
        }
    }

    private fun initialize(): ObjectStorageClient {
        val configFile = ConfigFileReader.parseDefault()
        val provider = ConfigFileAuthenticationDetailsProvider(configFile)
        return ObjectStorageClient
            .builder()
            .region(Region.AP_SEOUL_1)
            .clientConfigurator { it.property(StandardClientProperties.BUFFER_REQUEST, false) }
            .build(provider)
    }
}
