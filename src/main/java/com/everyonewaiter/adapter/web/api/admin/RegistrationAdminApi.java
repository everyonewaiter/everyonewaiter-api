package com.everyonewaiter.adapter.web.api.admin;

import com.everyonewaiter.adapter.web.api.dto.RegistrationAdminDetailResponse;
import com.everyonewaiter.adapter.web.api.dto.RegistrationAdminPageResponse;
import com.everyonewaiter.application.store.provided.RegistrationAdministrator;
import com.everyonewaiter.application.store.provided.RegistrationFinder;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationAdminPageRequest;
import com.everyonewaiter.domain.store.RegistrationRejectRequest;
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
class RegistrationAdminApi implements RegistrationAdminApiSpecification {

  private final RegistrationFinder registrationFinder;
  private final RegistrationAdministrator registrationAdministrator;

  @Override
  @GetMapping
  public ResponseEntity<Paging<RegistrationAdminPageResponse>> getRegistrations(
      @ModelAttribute @Valid RegistrationAdminPageRequest pageRequest,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    return ResponseEntity.ok(
        registrationFinder.findAllByAdmin(pageRequest)
            .map(RegistrationAdminPageResponse::from)
    );
  }

  @Override
  @GetMapping("/{registrationId}")
  public ResponseEntity<RegistrationAdminDetailResponse> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    Registration view = registrationFinder.findByAdminOrThrow(registrationId);

    return ResponseEntity.ok(RegistrationAdminDetailResponse.from(view));
  }

  @Override
  @PostMapping("/{registrationId}/approve")
  public ResponseEntity<Void> approve(
      @PathVariable Long registrationId,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    registrationAdministrator.approve(registrationId);

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/{registrationId}/reject")
  public ResponseEntity<Void> reject(
      @PathVariable Long registrationId,
      @RequestBody @Valid RegistrationRejectRequest rejectRequest,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    registrationAdministrator.reject(registrationId, rejectRequest);

    return ResponseEntity.noContent().build();
  }

}
