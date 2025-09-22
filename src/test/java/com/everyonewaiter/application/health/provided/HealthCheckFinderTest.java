package com.everyonewaiter.application.health.provided;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createApkVersionCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ServerInfo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

@RequiredArgsConstructor
class HealthCheckFinderTest extends IntegrationTest {

  private final BuildProperties buildProperties;
  private final HealthCheckCreator healthCheckCreator;
  private final HealthCheckFinder healthCheckFinder;

  @Test
  void findServerInfo() {
    ServerInfo serverVersion = healthCheckFinder.findServerInfo();

    assertThat(serverVersion.version()).isEqualTo(buildProperties.getVersion());
    assertThat(serverVersion.group()).isEqualTo(buildProperties.getGroup());
    assertThat(serverVersion.artifact()).isEqualTo(buildProperties.getArtifact());
  }

  @Test
  void findLatestApkVersion() {
    ApkVersion apkVersion = healthCheckCreator.createApkVersion(createApkVersionCreateRequest());

    ApkVersion latestApkVersion = healthCheckFinder.findLatestApkVersion();

    assertThat(latestApkVersion).isEqualTo(apkVersion);
  }

}
