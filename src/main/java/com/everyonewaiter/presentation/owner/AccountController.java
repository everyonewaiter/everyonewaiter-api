package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.account.AccountService;
import com.everyonewaiter.application.account.response.ProfileResponse;
import com.everyonewaiter.application.auth.AuthService;
import com.everyonewaiter.application.auth.response.Token;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.exception.AccessDeniedException;
import com.everyonewaiter.presentation.owner.request.AccountWrite;
import com.everyonewaiter.presentation.owner.request.Auth;
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
  public ResponseEntity<ProfileResponse> getProfile(@AuthenticationAccount Account account) {
    ProfileResponse response = ProfileResponse.from(account);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountWrite.CreateRequest request) {
    authService.checkExistsAuthSuccess(request.phoneNumber(), AuthPurpose.SIGN_UP);
    Long accountId = accountService.create(request.toDomainDto());
    return ResponseEntity.created(URI.create(accountId.toString())).build();
  }

  @Override
  @PostMapping("/sign-in")
  public ResponseEntity<Token.AllResponse> signIn(
      @RequestBody @Valid AccountWrite.SignInRequest request
  ) {
    Long accountId = accountService.signIn(request.toDomainDto());
    Token.AllResponse response = authService.generateTokenBySignIn(accountId);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping("/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(@RequestBody @Valid Auth.SendAuthCodeRequest request) {
    accountService.checkAvailablePhoneNumber(request.phoneNumber());
    authService.sendAuthCode(request.toDomainDto(AuthPurpose.SIGN_UP));
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/verify-auth-code")
  public ResponseEntity<Void> verifyAuthCode(
      @RequestBody @Valid Auth.VerifyAuthCodeRequest request
  ) {
    authService.verifyAuthCode(request.toDomainDto(AuthPurpose.SIGN_UP));
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/send-auth-mail")
  public ResponseEntity<Void> sendAuthMail(@RequestBody @Valid Auth.SendAuthMailRequest request) {
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
  public ResponseEntity<Token.AllResponse> renewToken(
      @RequestBody @Valid Auth.RenewTokenRequest request
  ) {
    return authService.renewToken(request.refreshToken())
        .map(ResponseEntity::ok)
        .orElseThrow(AccessDeniedException::new);
  }

}
