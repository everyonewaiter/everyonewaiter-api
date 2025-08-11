package com.everyonewaiter.domain.health;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createApkVersionCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApkVersionTest {

  @Test
  void create() {
    ApkVersionCreateRequest createRequest = createApkVersionCreateRequest();

    ApkVersion apkVersion = ApkVersion.create(createRequest);

    assertThat(apkVersion.getMajorVersion()).isEqualTo(createRequest.majorVersion());
    assertThat(apkVersion.getMinorVersion()).isEqualTo(createRequest.minorVersion());
    assertThat(apkVersion.getPatchVersion()).isEqualTo(createRequest.patchVersion());
    assertThat(apkVersion.getDownloadUri()).isEqualTo(createRequest.downloadUri());
  }

}
