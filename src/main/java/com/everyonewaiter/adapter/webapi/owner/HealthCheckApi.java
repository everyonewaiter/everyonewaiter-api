package com.everyonewaiter.adapter.webapi.owner;

import com.everyonewaiter.adapter.webapi.owner.dto.ApkVersionDetailResponse;
import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.application.health.provided.HealthCheckFinder;
import com.everyonewaiter.domain.health.ApkVersion;
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
  public ResponseEntity<ServerVersionDetailResponse> getServerVersion() {
    return ResponseEntity.ok(healthCheckFinder.findServerVersion());
  }

  @Override
  @GetMapping("/apk-versions")
  public ResponseEntity<ApkVersionDetailResponse> getLatestApkVersion() {
    ApkVersion latestApkVersion = healthCheckFinder.findLatestApkVersion();

    return ResponseEntity.ok(ApkVersionDetailResponse.from(latestApkVersion));
  }

}
