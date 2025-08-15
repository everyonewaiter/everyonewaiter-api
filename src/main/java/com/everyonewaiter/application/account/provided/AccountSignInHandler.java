package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.auth.SignInToken;
import com.everyonewaiter.domain.auth.SignInTokenRenewRequest;
import jakarta.validation.Valid;

public interface AccountSignInHandler {

  SignInToken signIn(@Valid AccountSignInRequest signInRequest);

  SignInToken renew(@Valid SignInTokenRenewRequest signInTokenRenewRequest);

}
