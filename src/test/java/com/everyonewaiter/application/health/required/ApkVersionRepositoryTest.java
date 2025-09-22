package com.everyonewaiter.application.health.required;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createApkVersionCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class ApkVersionRepositoryTest extends IntegrationTest {

  private final ApkVersionRepository apkVersionRepository;

  @Test
  void findLatest() {
    ApkVersion apkVersion = ApkVersion.create(createApkVersionCreateRequest());
    apkVersionRepository.save(apkVersion);

    ApkVersion found = apkVersionRepository.findLatest();

    assertThat(found.getId()).isNotNull();
    assertThat(found.getId()).isEqualTo(apkVersion.getId());
  }

  @Test
  void findLatestFail() {
    assertThatThrownBy(apkVersionRepository::findLatest)
        .isInstanceOf(ApkVersionNotFoundException.class);
  }

}
