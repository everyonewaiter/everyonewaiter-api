package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.health.ApkVersionService;
import com.everyonewaiter.application.health.response.ApkVersionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/health")
class HealthController implements HealthControllerSpecification {

  private final BuildProperties buildProperties;
  private final ApkVersionService apkVersionService;

  @Override
  @GetMapping("/server-versions")
  public ResponseEntity<String> getServerVersion() {
    return ResponseEntity.ok(buildProperties.getVersion());
  }

  @Override
  @GetMapping("/apk-versions")
  public ResponseEntity<ApkVersionResponse.Detail> getApkVersion() {
    return ResponseEntity.ok(apkVersionService.readLatestVersion());
  }

}
