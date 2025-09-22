package com.everyonewaiter.application.health;

import com.everyonewaiter.application.health.provided.HealthCheckFinder;
import com.everyonewaiter.application.health.required.ApkVersionRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ServerInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class HealthCheckQueryService implements HealthCheckFinder {

  private final BuildProperties buildProperties;
  private final ApkVersionRepository apkVersionRepository;

  @Override
  public ServerInfo findServerInfo() {
    return ServerInfo.create(buildProperties);
  }

  @Override
  public ApkVersion findLatestApkVersion() {
    return apkVersionRepository.findLatest();
  }

}
