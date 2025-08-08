package com.everyonewaiter.application.health.provided;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createApkVersionCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.domain.health.ApkVersion;
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
    ApkVersion apkVersion = healthCheckCreator.createApkVersion(createApkVersionCreateRequest());

    entityManager.flush();
    entityManager.clear();

    ApkVersion latestApkVersion = healthCheckFinder.findLatestApkVersion();

    assertThat(latestApkVersion).isEqualTo(apkVersion);
  }

}
