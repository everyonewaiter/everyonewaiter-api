package com.everyonewaiter.infrastructure.image;

import com.everyonewaiter.domain.image.service.ImageClient;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.http.client.StandardClientProperties;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class OracleObjectStorageClient implements ImageClient {

  private final OracleObjectStorageProperties properties;

  @Override
  public void upload(File imageFile, String imageName, String contentType) {
    try (ObjectStorageClient client = initializeClient()) {
      UploadConfiguration configuration = UploadConfiguration.builder()
          .allowMultipartUploads(true)
          .allowParallelUploads(true)
          .build();
      UploadManager uploadManager = new UploadManager(client, configuration);

      UploadManager.UploadRequest request = UploadManager.UploadRequest.builder(imageFile)
          .allowOverwrite(true)
          .build(
              PutObjectRequest.builder()
                  .namespaceName(properties.getNamespace())
                  .bucketName(properties.getBucketName())
                  .objectName(imageName)
                  .contentType(contentType)
                  .contentLength(imageFile.length())
                  .cacheControl("max-age=86400")
                  .build()
          );

      uploadManager.upload(request);
    } catch (Exception exception) {
      throw new BusinessException(ErrorCode.FAILED_UPLOAD_IMAGE);
    }
  }

  @Override
  public void delete(String imageName) {
    try (ObjectStorageClient client = initializeClient()) {
      DeleteObjectRequest request = DeleteObjectRequest.builder()
          .namespaceName(properties.getNamespace())
          .bucketName(properties.getBucketName())
          .objectName(imageName)
          .build();
      client.deleteObject(request);
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.FAILED_DELETE_IMAGE);
    }
  }

  private ObjectStorageClient initializeClient() throws IOException {
    ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
    return ObjectStorageClient.builder()
        .region(Region.AP_SEOUL_1)
        .clientConfigurator(builder ->
            builder.property(StandardClientProperties.BUFFER_REQUEST, false)
        )
        .build(new ConfigFileAuthenticationDetailsProvider(configFile));
  }

}
