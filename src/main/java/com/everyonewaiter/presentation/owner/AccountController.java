package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.account.service.AccountService;
import com.everyonewaiter.application.auth.service.AuthService;
import com.everyonewaiter.application.auth.service.response.Token;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.AccountWrite;
import com.everyonewaiter.presentation.owner.request.Auth;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountWrite.CreateRequest request) {
    authService.checkExistsAuthSuccess(request.phoneNumber(), AuthPurpose.SIGN_UP);
    Long accountId = accountService.create(request.toAccountCreate());
    return ResponseEntity.created(URI.create(accountId.toString())).build();
  }

  @Override
  @PostMapping("/sign-in")
  public ResponseEntity<Token.AllResponse> signIn(
      @RequestBody @Valid AccountWrite.SignInRequest request
  ) {
    Long accountId = accountService.signIn(request.toAccountSignIn());
    Token.AllResponse response = authService.generateTokenBySignIn(accountId);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping("/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(@RequestBody @Valid Auth.SendAuthCodeRequest request) {
    accountService.checkAvailablePhoneNumber(request.phoneNumber());
    authService.sendAuthCode(request.toSendAuthCode(AuthPurpose.SIGN_UP));
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/verify-auth-code")
  public ResponseEntity<Void> verifyAuthCode(
      @RequestBody @Valid Auth.VerifyAuthCodeRequest request
  ) {
    authService.verifyAuthCode(request.toVerifyAuthCode(AuthPurpose.SIGN_UP));
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
        .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN));
  }

}
