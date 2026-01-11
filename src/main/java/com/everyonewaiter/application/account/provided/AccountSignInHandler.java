package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.account.SignInToken;
import jakarta.validation.Valid;

public interface AccountSignInHandler {

  SignInToken signIn(@Valid AccountSignInRequest signInRequest);

}
