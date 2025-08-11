package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.owner.request.RegistrationReadRequest;
import com.everyonewaiter.adapter.web.api.owner.request.RegistrationWriteRequest;
import com.everyonewaiter.application.store.RegistrationService;
import com.everyonewaiter.application.store.response.RegistrationResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.shared.Paging;
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
class RegistrationController implements RegistrationControllerSpecification {

  private final RegistrationService registrationService;

  @Override
  @GetMapping
  public ResponseEntity<Paging<RegistrationResponse.Detail>> getRegistrations(
      @ModelAttribute @Valid RegistrationReadRequest.PageView request,
      @AuthenticationAccount Account account
  ) {
    Paging<RegistrationResponse.Detail> responses =
        registrationService.readAll(account.getId(), request.toDomainDto());
    return ResponseEntity.ok(responses);
  }

  @Override
  @GetMapping("/{registrationId}")
  public ResponseEntity<RegistrationResponse.Detail> getRegistration(
      @PathVariable Long registrationId,
      @AuthenticationAccount Account account
  ) {
    return ResponseEntity.ok(registrationService.read(registrationId, account.getId()));
  }

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> apply(
      @ModelAttribute @Valid RegistrationWriteRequest.Create request,
      @AuthenticationAccount Account account
  ) {
    Long registrationId = registrationService.apply(account.getId(), request.toDomainDto());
    return ResponseEntity.created(URI.create(registrationId.toString())).build();
  }

  @Override
  @PutMapping("/{registrationId}")
  public ResponseEntity<Void> reapply(
      @PathVariable Long registrationId,
      @RequestBody @Valid RegistrationWriteRequest.Update request,
      @AuthenticationAccount Account account
  ) {
    registrationService.reapply(registrationId, account.getId(), request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PutMapping(value = "/{registrationId}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> reapply(
      @PathVariable Long registrationId,
      @ModelAttribute @Valid RegistrationWriteRequest.UpdateWithImage request,
      @AuthenticationAccount Account account
  ) {
    registrationService.reapply(registrationId, account.getId(), request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

}
