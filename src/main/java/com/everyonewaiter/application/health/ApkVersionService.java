package com.everyonewaiter.application.health;

import com.everyonewaiter.application.health.request.ApkVersionWrite;
import com.everyonewaiter.application.health.response.ApkVersionResponse;
import com.everyonewaiter.domain.health.entity.ApkVersion;
import com.everyonewaiter.domain.health.repository.ApkVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApkVersionService {

  private final ApkVersionRepository apkVersionRepository;

  @Transactional
  public Long createVersion(ApkVersionWrite.Create request) {
    ApkVersion apkVersion = ApkVersion.create(
        request.majorVersion(),
        request.minorVersion(),
        request.patchVersion(),
        request.downloadUrl()
    );
    return apkVersionRepository.save(apkVersion).getId();
  }

  public ApkVersionResponse.Detail readLatestVersion() {
    return ApkVersionResponse.Detail.from(apkVersionRepository.findLatest());
  }

}
