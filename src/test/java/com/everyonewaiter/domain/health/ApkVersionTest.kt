package com.everyonewaiter.domain.health

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ApkVersionTest {

  @Test
  fun `APK 버전 생성`() {
    val createRequest = createApkVersionCreateRequest()

    val apkVersion = ApkVersion.create(createRequest)

    Assertions.assertThat(apkVersion.majorVersion).isEqualTo(createRequest.majorVersion)
    Assertions.assertThat(apkVersion.minorVersion).isEqualTo(createRequest.minorVersion)
    Assertions.assertThat(apkVersion.patchVersion).isEqualTo(createRequest.patchVersion)
    Assertions.assertThat(apkVersion.downloadUri).isEqualTo(createRequest.downloadUri)
  }
}
