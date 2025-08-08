package com.everyonewaiter.application.health.provided;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.health.dto.ApkVersionDetailResponse;
import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

@IntegrationTest
record HealthCheckFinderTest(
    EntityManager entityManager,
    BuildProperties buildProperties,
    HealthCheckCreator healthCheckCreator,
    HealthCheckFinder healthCheckFinder
) {

  @Test
  void findServerVersion() {
    ServerVersionDetailResponse serverVersion = healthCheckFinder.findServerVersion();

    assertThat(serverVersion.version()).isEqualTo(buildProperties.getVersion());
    assertThat(serverVersion.group()).isEqualTo(buildProperties.getGroup());
    assertThat(serverVersion.artifact()).isEqualTo(buildProperties.getArtifact());
  }

  @Test
  void findLatestApkVersion() {
    ApkVersionCreateRequest createRequest = createRequest();
    healthCheckCreator.createApkVersion(createRequest);

    entityManager.flush();
    entityManager.clear();

    ApkVersionDetailResponse latestApkVersion = healthCheckFinder.findLatestApkVersion();

    assertThat(latestApkVersion.majorVersion()).isEqualTo(createRequest.majorVersion());
    assertThat(latestApkVersion.minorVersion()).isEqualTo(createRequest.minorVersion());
    assertThat(latestApkVersion.patchVersion()).isEqualTo(createRequest.patchVersion());
    assertThat(latestApkVersion.downloadUrl()).isEqualTo(createRequest.downloadUrl());
  }

}
