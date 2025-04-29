package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.presentation.owner.request.StoreWrite;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
class StoreController implements StoreControllerSpecification {

  private final StoreService storeService;

  @Override
  @PostMapping(value = "/registrations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> apply(
      @ModelAttribute @Valid StoreWrite.RegistrationCreateRequest request,
      @AuthenticationAccount Account account
  ) {
    Long registrationId = storeService.apply(account.getId(), request.toRegistrationCreate());
    return ResponseEntity.created(URI.create(registrationId.toString())).build();
  }

}
