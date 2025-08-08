package com.everyonewaiter.application.health.provided;

import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.domain.health.ApkVersion;

public interface HealthCheckFinder {

  ServerVersionDetailResponse findServerVersion();

  ApkVersion findLatestApkVersion();

}
