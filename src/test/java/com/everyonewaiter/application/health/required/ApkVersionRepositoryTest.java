package com.everyonewaiter.application.health.required;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class ApkVersionRepositoryTest extends IntegrationTest {

  private final EntityManager entityManager;
  private final ApkVersionRepository apkVersionRepository;

  @Test
  void findLatest() {
    ApkVersion apkVersion = ApkVersion.create(createRequest());
    apkVersionRepository.save(apkVersion);

    entityManager.flush();
    entityManager.clear();

    ApkVersion latestApkVersion = apkVersionRepository.findLatest();

    assertThat(latestApkVersion.getId()).isNotNull();
    assertThat(latestApkVersion.getId()).isEqualTo(apkVersion.getId());
  }

  @Test
  void findLatestFail() {
    assertThatThrownBy(apkVersionRepository::findLatest)
        .isInstanceOf(ApkVersionNotFoundException.class);
  }

}
