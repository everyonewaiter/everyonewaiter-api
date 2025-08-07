package com.everyonewaiter.adapter.webapi.owner;

import com.everyonewaiter.application.health.dto.ApkVersionDetailResponse;
import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.application.health.provided.HealthCheckFinder;
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
    return ResponseEntity.ok(healthCheckFinder.findLatestApkVersion());
  }

}
