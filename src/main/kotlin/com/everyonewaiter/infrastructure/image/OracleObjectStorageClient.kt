package com.everyonewaiter.infrastructure.image

import com.everyonewaiter.common.file.extension.deleteWithLog
import com.everyonewaiter.common.file.extension.toFile
import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.Region
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.http.client.StandardClientProperties
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails.AccessType
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails.builder
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration
import com.oracle.bmc.objectstorage.transfer.UploadManager
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.CompletableFuture

@Component
class OracleObjectStorageClient(
    @Value("\${oracle.object.storage.namespace}") val namespace: String,
    @Value("\${oracle.object.storage.bucket-name}") val bucketName: String,
) : ImageClient {
    @Async
    override fun read(
        name: String,
        accessUrlExpiration: Date,
    ): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        initialize().use { client ->
            val details = builder()
                .name("par-${DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm").format(Instant.now())}")
                .accessType(AccessType.ObjectRead)
                .objectName(name)
                .timeExpires(accessUrlExpiration)
                .build()
            val request = CreatePreauthenticatedRequestRequest
                .builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .createPreauthenticatedRequestDetails(details)
                .build()
            val response = client.createPreauthenticatedRequest(request)
            future.complete(response.preauthenticatedRequest.fullPath)
            return future
        }
    }

    override fun upload(file: MultipartFile) {
        val objectName = file.originalFilename ?: file.name
        val contentType = file.contentType ?: "image/webp"
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
                            .objectName(objectName)
                            .contentType(contentType)
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
            }
        } finally {
            convertedFile.deleteWithLog()
        }
    }

    private fun initialize(): ObjectStorageClient {
        try {
            val configFile = ConfigFileReader.parseDefault()
            val provider = ConfigFileAuthenticationDetailsProvider(configFile)
            return ObjectStorageClient
                .builder()
                .region(Region.AP_SEOUL_1)
                .clientConfigurator { it.property(StandardClientProperties.BUFFER_REQUEST, false) }
                .build(provider)
        } catch (exception: IOException) {
            throw IllegalArgumentException(exception.message)
        }
    }
}
