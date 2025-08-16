package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.dto.RegistrationDetailResponse;
import com.everyonewaiter.application.store.provided.RegistrationApplier;
import com.everyonewaiter.application.store.provided.RegistrationFinder;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationApplyRequest;
import com.everyonewaiter.domain.store.RegistrationPageRequest;
import com.everyonewaiter.domain.store.RegistrationReapplyRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores/registrations")
class RegistrationApi implements RegistrationApiSpecification {

  private final RegistrationFinder registrationFinder;
  private final RegistrationApplier registrationApplier;

  @Override
  @GetMapping
  public ResponseEntity<Paging<RegistrationDetailResponse>> getRegistrations(
      @ModelAttribute @Valid RegistrationPageRequest pageRequest,
      @AuthenticationAccount Account account
  ) {
    return ResponseEntity.ok(
        registrationFinder.findAll(account.getId(), pageRequest)
            .map(RegistrationDetailResponse::from)
    );
  }

  @Override
  @GetMapping("/{registrationId}")
  public ResponseEntity<RegistrationDetailResponse> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount Account account
  ) {
    Registration registration = registrationFinder.findOrThrow(registrationId, account.getId());

    return ResponseEntity.ok(RegistrationDetailResponse.from(registration));
  }

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> apply(
      @ModelAttribute @Valid RegistrationApplyRequest applyRequest,
      @AuthenticationAccount Account account
  ) {
    Registration registration = registrationApplier.apply(account, applyRequest);

    return ResponseEntity.created(URI.create(registration.getNonNullId().toString())).build();
  }

  @Override
  @PutMapping("/{registrationId}")
  public ResponseEntity<Void> reapply(
      @PathVariable Long registrationId,
      @RequestBody @Valid RegistrationReapplyRequest reapplyRequest,
      @AuthenticationAccount Account account
  ) {
    registrationApplier.reapply(registrationId, account.getId(), reapplyRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @PutMapping(value = "/{registrationId}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> reapply(
      @PathVariable Long registrationId,
      @ModelAttribute @Valid RegistrationApplyRequest applyRequest,
      @AuthenticationAccount Account account
  ) {
    registrationApplier.reapply(registrationId, account.getId(), applyRequest);

    return ResponseEntity.noContent().build();
  }

}
