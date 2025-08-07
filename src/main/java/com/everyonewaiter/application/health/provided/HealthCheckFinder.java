package com.everyonewaiter.application.health.provided;

import com.everyonewaiter.application.health.dto.ApkVersionDetailResponse;
import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;

public interface HealthCheckFinder {

  ServerVersionDetailResponse findServerVersion();

  ApkVersionDetailResponse findLatestApkVersion();

}
