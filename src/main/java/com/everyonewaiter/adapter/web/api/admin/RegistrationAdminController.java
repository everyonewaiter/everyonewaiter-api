package com.everyonewaiter.adapter.web.api.admin;

import com.everyonewaiter.adapter.web.api.admin.request.RegistrationAdminReadRequest;
import com.everyonewaiter.adapter.web.api.admin.request.RegistrationAdminWriteRequest;
import com.everyonewaiter.application.store.RegistrationService;
import com.everyonewaiter.application.store.response.RegistrationAdminResponse;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.shared.Paging;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admins/stores/registrations")
class RegistrationAdminController implements RegistrationAdminControllerSpecification {

  private final RegistrationService registrationService;

  @Override
  @GetMapping
  public ResponseEntity<Paging<RegistrationAdminResponse.PageView>> getRegistrations(
      @ModelAttribute @Valid RegistrationAdminReadRequest.PageView request,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(registrationService.readAllByAdmin(request.toDomainDto()));
  }

  @Override
  @GetMapping("/{registrationId}")
  public ResponseEntity<RegistrationAdminResponse.DetailView> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(registrationService.readByAdmin(registrationId));
  }

  @Override
  @PostMapping("/{registrationId}/approve")
  public ResponseEntity<Void> approve(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    registrationService.approve(registrationId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/{registrationId}/reject")
  public ResponseEntity<Void> reject(
      @PathVariable Long registrationId,
      @RequestBody @Valid RegistrationAdminWriteRequest.Reject request,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    registrationService.reject(registrationId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

}
