package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.health.ApkVersionService;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.presentation.admin.request.ApkVersionAdminWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admins/apk-versions")
class ApkVersionAdminController implements ApkVersionAdminControllerSpecification {

  private final ApkVersionService apkVersionService;

  @Override
  @PostMapping
  public ResponseEntity<Void> createApkVersion(
      @RequestBody @Valid ApkVersionAdminWriteRequest.Create request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    Long apkVersionId = apkVersionService.createVersion(request.toDomainDto());
    return ResponseEntity.created(URI.create(apkVersionId.toString())).build();
  }

}
