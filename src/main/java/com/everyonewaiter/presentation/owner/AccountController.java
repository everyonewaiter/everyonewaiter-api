package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.account.service.AccountService;
import com.everyonewaiter.application.auth.service.AuthService;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.presentation.owner.request.AccountWrite;
import com.everyonewaiter.presentation.owner.request.Auth;
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
@RequestMapping("/api/v1/accounts")
class AccountController implements AccountControllerSpecification {

  private final AuthService authService;
  private final AccountService accountService;

  @Override
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountWrite.CreateRequest request) {
    Long accountId = accountService.create(request.toAccountCreate());
    return ResponseEntity.created(URI.create(accountId.toString())).build();
  }

  @Override
  @PostMapping("/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(@RequestBody @Valid Auth.SendAuthCodeRequest request) {
    accountService.checkAvailablePhoneNumber(request.getPhoneNumber());
    authService.sendAuthCode(request.toSendAuthCode(AuthPurpose.SIGN_UP));
    return ResponseEntity.noContent().build();
  }

}
