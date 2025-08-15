package com.everyonewaiter.adapter.web.config;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.shared.AccessDeniedException;
import com.everyonewaiter.domain.shared.AuthenticationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
class AuthenticationAccountResolver implements HandlerMethodArgumentResolver {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final AccountRepository accountRepository;

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    boolean hasAnnotation = parameter.hasParameterAnnotation(AuthenticationAccount.class);
    boolean isCorrectParameterType = Account.class.isAssignableFrom(parameter.getParameterType());

    return hasAnnotation && isCorrectParameterType;
  }

  @Override
  public Account resolveArgument(
      @NonNull MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) {
    String accessToken = extractToken(webRequest);
    JwtPayload payload = jwtProvider.decode(accessToken).orElseThrow(AuthenticationException::new);

    return accountRepository.findById(payload.id())
        .map(account -> {
          AuthenticationAccount annotation = requireNonNull(
              parameter.getParameterAnnotation(AuthenticationAccount.class)
          );

          if (!account.isActive() || !account.hasPermission(annotation.permission())) {
            throw new AccessDeniedException();
          }

          return account;
        })
        .orElseThrow(AuthenticationException::new);
  }

  private String extractToken(NativeWebRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
      return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    throw new AuthenticationException();
  }

}
