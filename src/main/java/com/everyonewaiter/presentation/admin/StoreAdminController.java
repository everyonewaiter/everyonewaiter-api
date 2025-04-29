package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.store.RegistrationService;
import com.everyonewaiter.application.store.response.RegistrationAdmin;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminRead;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

}
