package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.dto.AccountProfileResponse;
import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.account.provided.AccountRegister;
import com.everyonewaiter.application.account.provided.AccountSignInHandler;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountCreateRequest;
import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.auth.SendAuthCodeRequest;
import com.everyonewaiter.domain.auth.SendAuthMailRequest;
import com.everyonewaiter.domain.auth.SignInToken;
import com.everyonewaiter.domain.auth.SignInTokenRenewRequest;
import com.everyonewaiter.domain.auth.VerifyAuthCodeRequest;
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
class AccountApi implements AccountApiSpecification {

  private final Authenticator authenticator;
  private final AccountFinder accountFinder;
  private final AccountRegister accountRegister;
  private final AccountSignInHandler accountSignInHandler;

  @Override
  @GetMapping("/me")
  public ResponseEntity<AccountProfileResponse> getProfile(
      @AuthenticationAccount Account account
  ) {
    return ResponseEntity.ok(AccountProfileResponse.from(account));
  }

  @Override
  @GetMapping("/phone-number/{phoneNumber}/me")
  public ResponseEntity<AccountProfileResponse> getProfile(@PathVariable String phoneNumber) {
    Account account = accountFinder.findOrThrow(new PhoneNumber(phoneNumber));

    return ResponseEntity.ok(AccountProfileResponse.from(account));
  }

  @Override
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountCreateRequest createRequest) {
    Account account = accountRegister.register(createRequest);

    return ResponseEntity.created(URI.create(account.getNonNullId().toString())).build();
  }

  @Override
  @PostMapping("/sign-in")
  public ResponseEntity<SignInToken> signIn(
      @RequestBody @Valid AccountSignInRequest signInRequest
  ) {
    SignInToken signInToken = accountSignInHandler.signIn(signInRequest);

    return ResponseEntity.ok(signInToken);
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
    accountRegister.activate(token);

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/renew-token")
  public ResponseEntity<SignInToken> renewToken(
      @RequestBody @Valid SignInTokenRenewRequest signInTokenRenewRequest
  ) {
    return ResponseEntity.ok(accountSignInHandler.renew(signInTokenRenewRequest));
  }

}
