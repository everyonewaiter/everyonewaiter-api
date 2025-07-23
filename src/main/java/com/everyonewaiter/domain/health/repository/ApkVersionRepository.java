package com.everyonewaiter.domain.health.repository;

import com.everyonewaiter.domain.health.entity.ApkVersion;

public interface ApkVersionRepository {

  ApkVersion findLatest();

  ApkVersion save(ApkVersion apkVersion);

}
