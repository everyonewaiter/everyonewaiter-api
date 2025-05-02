package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.store.RegistrationService;
import com.everyonewaiter.application.store.response.RegistrationAdminResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminReadRequest;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminWriteRequest;
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
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(registrationService.readAllByAdmin(request.toDomainDto()));
  }

  @Override
  @GetMapping("/{registrationId}")
  public ResponseEntity<RegistrationAdminResponse.DetailView> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(registrationService.readByAdmin(registrationId));
  }

  @Override
  @PostMapping("/{registrationId}/approve")
  public ResponseEntity<Void> approve(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    registrationService.approve(registrationId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/{registrationId}/reject")
  public ResponseEntity<Void> reject(
      @PathVariable Long registrationId,
      @RequestBody @Valid RegistrationAdminWriteRequest.Reject request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    registrationService.reject(registrationId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

}
