package com.everyonewaiter.adapter.webapi.admin;

import com.everyonewaiter.application.health.provided.HealthCheckCreator;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admins/apk-versions")
class HealthCheckAdminApi implements HealthCheckAdminApiSpecification {

  private final HealthCheckCreator healthCheckCreator;

  @Override
  @PostMapping
  public ResponseEntity<Void> createApkVersion(
      @RequestBody @Valid ApkVersionCreateRequest request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    ApkVersion apkVersion = healthCheckCreator.createApkVersion(request);

    return ResponseEntity
        .created(URI.create(Objects.requireNonNull(apkVersion.getId()).toString()))
        .build();
  }

}
