package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.store.RegistrationService;
import com.everyonewaiter.application.store.response.RegistrationAdmin;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminRead;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminWrite;
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
@RequestMapping("/api/v1/admins/stores")
class StoreAdminController implements StoreAdminControllerSpecification {

  private final RegistrationService registrationService;

  @Override
  @GetMapping("/registrations")
  public ResponseEntity<Paging<RegistrationAdmin.PageViewResponse>> getRegistrations(
      @ModelAttribute @Valid RegistrationAdminRead.PageRequest request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(registrationService.readAllByAdmin(request.toDomainDto()));
  }

  @Override
  @GetMapping("/registrations/{registrationId}")
  public ResponseEntity<RegistrationAdmin.DetailViewResponse> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(registrationService.readByAdmin(registrationId));
  }

  @Override
  @PostMapping("/registrations/{registrationId}/reject")
  public ResponseEntity<Void> reject(
      @PathVariable Long registrationId,
      @RequestBody @Valid RegistrationAdminWrite.RejectRequest request,
      @AuthenticationAccount(permission = Account.Permission.ADMIN) Account account
  ) {
    registrationService.reject(registrationId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

}
