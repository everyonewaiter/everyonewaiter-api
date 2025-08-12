package com.everyonewaiter.application.auth.provided;

import com.everyonewaiter.application.auth.dto.TokenResponse;
import java.util.Optional;

public interface SignInTokenProvider {

  TokenResponse createToken(Long accountId);

  Optional<TokenResponse> renewToken(String refToken);

}
