package com.everyonewaiter.application.health.required;

import com.everyonewaiter.domain.health.ApkVersion;
import java.util.Optional;

public interface ApkVersionRepository {

  Optional<ApkVersion> findLatest();

  ApkVersion findLatestOrThrow();

  ApkVersion save(ApkVersion apkVersion);

}
