package com.everyonewaiter.adapter.integration.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oracle.object.storage")
@RequiredArgsConstructor
class OracleObjectStorageProperties {

  private final String namespace;
  private final String bucketName;

}
