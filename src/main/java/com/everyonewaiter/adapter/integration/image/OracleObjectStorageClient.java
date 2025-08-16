package com.everyonewaiter.adapter.integration.image;

import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.image.required.ImageClient;
import com.everyonewaiter.domain.image.FailedDeleteImageException;
import com.everyonewaiter.domain.image.FailedUploadImageException;
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
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class OracleObjectStorageClient implements ImageClient {

  private static final Logger LOGGER = getLogger(OracleObjectStorageClient.class);

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
    } catch (IOException exception) {
      LOGGER.error("[이미지 업로드 실패] {}", imageName, exception);

      throw new FailedUploadImageException();
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
    } catch (IOException exception) {
      LOGGER.error("[이미지 삭제 실패] {}", imageName, exception);

      throw new FailedDeleteImageException();
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
