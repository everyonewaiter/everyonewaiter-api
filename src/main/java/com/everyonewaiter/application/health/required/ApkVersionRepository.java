package com.everyonewaiter.application.health.required;

import com.everyonewaiter.domain.health.ApkVersion;

public interface ApkVersionRepository {

  ApkVersion findLatest();

  ApkVersion save(ApkVersion apkVersion);

}
