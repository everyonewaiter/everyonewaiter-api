package com.everyonewaiter.application.health;

import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.application.health.provided.HealthCheckFinder;
import com.everyonewaiter.application.health.required.ApkVersionRepository;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.global.annotation.ReadOnlyTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class HealthCheckQueryService implements HealthCheckFinder {

  private final BuildProperties buildProperties;
  private final ApkVersionRepository apkVersionRepository;

  @Override
  public ServerVersionDetailResponse findServerVersion() {
    return ServerVersionDetailResponse.from(buildProperties);
  }

  @Override
  @ReadOnlyTransactional
  public ApkVersion findLatestApkVersion() {
    return apkVersionRepository.findLatest();
  }

}
