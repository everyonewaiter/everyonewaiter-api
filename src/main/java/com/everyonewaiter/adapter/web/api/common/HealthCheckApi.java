package com.everyonewaiter.adapter.web.api.common;

import com.everyonewaiter.adapter.web.api.owner.dto.ApkVersionDetailResponse;
import com.everyonewaiter.application.health.provided.HealthCheckFinder;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ServerInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/health")
class HealthCheckApi implements HealthCheckApiSpecification {

  private final HealthCheckFinder healthCheckFinder;

  @Override
  @GetMapping("/server-versions")
  public ResponseEntity<ServerInfo> getServerVersion() {
    ServerInfo serverInfo = healthCheckFinder.findServerInfo();

    return ResponseEntity.ok(serverInfo);
  }

  @Override
  @GetMapping("/apk-versions")
  public ResponseEntity<ApkVersionDetailResponse> getLatestApkVersion() {
    ApkVersion latestApkVersion = healthCheckFinder.findLatestApkVersion();

    return ResponseEntity.ok(ApkVersionDetailResponse.from(latestApkVersion));
  }

}
