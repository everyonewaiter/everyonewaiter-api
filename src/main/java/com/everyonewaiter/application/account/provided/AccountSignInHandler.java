package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.application.auth.dto.SignInTokenRenewRequest;
import com.everyonewaiter.application.auth.dto.TokenResponse;
import com.everyonewaiter.domain.account.AccountSignInRequest;
import jakarta.validation.Valid;

public interface AccountSignInHandler {

  TokenResponse signIn(@Valid AccountSignInRequest signInRequest);

  TokenResponse renew(@Valid SignInTokenRenewRequest signInTokenRenewRequest);

}
