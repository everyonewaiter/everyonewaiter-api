package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.store.RegistrationService;
import com.everyonewaiter.application.store.response.RegistrationDetailResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.owner.request.RegistrationRead;
import com.everyonewaiter.presentation.owner.request.RegistrationWrite;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
class StoreController implements StoreControllerSpecification {

  private final RegistrationService registrationService;

  @Override
  @GetMapping("/registrations")
  public ResponseEntity<Paging<RegistrationDetailResponse>> getRegistrations(
      @ModelAttribute @Valid RegistrationRead.RegistrationPageRequest request,
      @AuthenticationAccount Account account
  ) {
    Paging<RegistrationDetailResponse> responses =
        registrationService.readAll(account.getId(), request.toRegistrationPage());
    return ResponseEntity.ok(responses);
  }

  @Override
  @GetMapping("/registrations/{registrationId}")
  public ResponseEntity<RegistrationDetailResponse> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount Account account
  ) {
    return ResponseEntity.ok(registrationService.read(registrationId, account.getId()));
  }

  @Override
  @PostMapping(value = "/registrations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> apply(
      @ModelAttribute @Valid RegistrationWrite.RegistrationCreateRequest request,
      @AuthenticationAccount Account account
  ) {
    Long registrationId =
        registrationService.apply(account.getId(), request.toRegistrationCreate());
    return ResponseEntity.created(URI.create(registrationId.toString())).build();
  }

}
