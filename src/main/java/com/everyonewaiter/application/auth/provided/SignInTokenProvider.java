package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.domain.auth.SignInToken;
import com.everyonewaiter.domain.auth.SignInTokenRenewRequest;
import jakarta.validation.Valid;
import java.util.Optional;

public interface SignInTokenProvider {

  SignInToken createToken(Long accountId);

  Optional<SignInToken> renewToken(@Valid SignInTokenRenewRequest signInTokenRenewRequest);

}
