package com.everyonewaiter.application.health.provided;

import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ServerInfo;

public interface HealthCheckFinder {

  ServerInfo findServerInfo();

  ApkVersion findLatestApkVersion();

}
