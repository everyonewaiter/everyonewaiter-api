package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.owner.request.AccountWriteRequest;
import com.everyonewaiter.application.account.AccountService;
import com.everyonewaiter.application.account.response.AccountResponse;
import com.everyonewaiter.application.auth.dto.SendAuthCodeRequest;
import com.everyonewaiter.application.auth.dto.SendAuthMailRequest;
import com.everyonewaiter.application.auth.dto.SignInTokenRenewRequest;
import com.everyonewaiter.application.auth.dto.TokenResponse;
import com.everyonewaiter.application.auth.dto.VerifyAuthCodeRequest;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.application.auth.provided.SignInTokenProvider;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.shared.AccessDeniedException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/accounts")
class AccountController implements AccountControllerSpecification {

  private final Authenticator authenticator;
  private final SignInTokenProvider tokenProvider;
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
  @GetMapping("/phone-number/{phoneNumber}/me")
  public ResponseEntity<AccountResponse.Profile> getProfile(@PathVariable String phoneNumber) {
    AccountResponse.Profile response = accountService.readByPhone(phoneNumber);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountWriteRequest.Create request) {
    authenticator.checkAuthSuccess(AuthPurpose.SIGN_UP, new PhoneNumber(request.phoneNumber()));
    Long accountId = accountService.create(request.toDomainDto());
    return ResponseEntity.created(URI.create(accountId.toString())).build();
  }

  @Override
  @PostMapping("/sign-in")
  public ResponseEntity<TokenResponse> signIn(
      @RequestBody @Valid AccountWriteRequest.SignIn request
  ) {
    Long accountId = accountService.signIn(request.toDomainDto());
    TokenResponse response = tokenProvider.createToken(accountId);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping("/send-auth-code")
  public ResponseEntity<Void> sendAuthCode(
      @RequestBody @Valid SendAuthCodeRequest sendAuthCodeRequest
  ) {
    authenticator.sendAuthCode(AuthPurpose.SIGN_UP, sendAuthCodeRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/verify-auth-code")
  public ResponseEntity<Void> verifyAuthCode(
      @RequestBody @Valid VerifyAuthCodeRequest verifyAuthCodeRequest
  ) {
    authenticator.verifyAuthCode(AuthPurpose.SIGN_UP, verifyAuthCodeRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/send-auth-mail")
  public ResponseEntity<Void> sendAuthMail(
      @RequestBody @Valid SendAuthMailRequest sendAuthMailRequest
  ) {
    authenticator.sendAuthMail(sendAuthMailRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/verify-auth-mail")
  public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
    Email email = authenticator.verifyAuthMail(token);
    accountService.activate(email.address());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/renew-token")
  public ResponseEntity<TokenResponse> renewToken(
      @RequestBody @Valid SignInTokenRenewRequest signInTokenRenewRequest
  ) {
    return tokenProvider.renewToken(signInTokenRenewRequest)
        .map(ResponseEntity::ok)
        .orElseThrow(AccessDeniedException::new);
  }

}
