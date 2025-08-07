package com.everyonewaiter.application.health;

import com.everyonewaiter.application.health.provided.HealthCheckCreator;
import com.everyonewaiter.application.health.required.ApkVersionRepository;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class HealthCheckModifyService implements HealthCheckCreator {

  private final ApkVersionRepository apkVersionRepository;

  @Override
  public Long createApkVersion(ApkVersionCreateRequest createRequest) {
    ApkVersion apkVersion = ApkVersion.create(createRequest);

    return apkVersionRepository.save(apkVersion).getId();
  }

}
