package com.everyonewaiter.application.health.provided;

import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import jakarta.validation.Valid;

public interface HealthCheckCreator {

  ApkVersion createApkVersion(@Valid ApkVersionCreateRequest createRequest);

}
