package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.account.AccountService;
import com.everyonewaiter.application.account.response.AccountResponse;
import com.everyonewaiter.application.auth.AuthService;
import com.everyonewaiter.application.auth.response.TokenResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.exception.AccessDeniedException;
import com.everyonewaiter.presentation.owner.request.AccountWriteRequest;
import com.everyonewaiter.presentation.owner.request.AuthWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
class AccountController implements AccountControllerSpecification {

  private final AuthService authService;
  private final AccountService accountService;

  @Override
  @GetMapping("/me")
  public ResponseEntity<AccountResponse.Profile> getProfile(
      @AuthenticationAccount Account account
  ) {
    AccountResponse.Profile response = AccountResponse.Profile.from(account);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountWriteRequest.Create request) {
    authService.checkExistsAuthSuccess(request.phoneNumber(), AuthPurpose.SIGN_UP);
    Long accountId = accountService.create(request.toDomainDto());
    return ResponseEntity.created(URI.create(accountId.toString())).build();
  }

  @Override
  @PostMapping("/sign-in")
  public ResponseEntity<TokenResponse.All> signIn(
      @RequestBody @Valid AccountWriteRequest.SignIn request
  ) {
    Long accountId = accountService.signIn(request.toDomainDto());
    TokenResponse.All response = authService.generateTokenBySignIn(accountId);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping("/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(
      @RequestBody @Valid AuthWriteRequest.SendAuthCode request
  ) {
    accountService.checkAvailablePhone(request.phoneNumber());
    authService.sendAuthCode(request.toDomainDto(AuthPurpose.SIGN_UP));
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/verify-auth-code")
  public ResponseEntity<Void> verifyAuthCode(
      @RequestBody @Valid AuthWriteRequest.VerifyAuthCode request
  ) {
    authService.verifyAuthCode(request.toDomainDto(AuthPurpose.SIGN_UP));
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/send-auth-mail")
  public ResponseEntity<Void> sendAuthMail(
      @RequestBody @Valid AuthWriteRequest.SendAuthMail request
  ) {
    accountService.checkPossibleSendAuthMail(request.email());
    authService.sendAuthMail(request.email());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/verify-auth-mail")
  public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
    String email = authService.verifyAuthMail(token);
    accountService.activate(email);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/renew-token")
  public ResponseEntity<TokenResponse.All> renewToken(
      @RequestBody @Valid AuthWriteRequest.RenewToken request
  ) {
    return authService.renewToken(request.refreshToken())
        .map(ResponseEntity::ok)
        .orElseThrow(AccessDeniedException::new);
  }

}
