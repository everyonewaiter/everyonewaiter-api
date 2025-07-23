package com.everyonewaiter.application.health;

import com.everyonewaiter.application.health.response.ApkVersionResponse;
import com.everyonewaiter.domain.health.repository.ApkVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApkVersionService {

  private final ApkVersionRepository apkVersionRepository;

  public ApkVersionResponse.Detail readVersion() {
    return ApkVersionResponse.Detail.from(apkVersionRepository.findLatest());
  }

}
