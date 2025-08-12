package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.application.auth.dto.SignInTokenRenewRequest;
import com.everyonewaiter.application.auth.dto.TokenResponse;
import jakarta.validation.Valid;
import java.util.Optional;

public interface SignInTokenProvider {

  TokenResponse createToken(Long accountId);

  Optional<TokenResponse> renewToken(@Valid SignInTokenRenewRequest signInTokenRenewRequest);

}
