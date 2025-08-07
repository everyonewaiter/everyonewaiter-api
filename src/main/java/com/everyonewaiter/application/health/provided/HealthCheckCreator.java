package com.everyonewaiter.application.health.provided;

import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import jakarta.validation.Valid;

public interface HealthCheckCreator {

  Long createApkVersion(@Valid ApkVersionCreateRequest createRequest);

}
