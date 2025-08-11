package com.everyonewaiter.adapter.web.config;

import com.everyonewaiter.application.auth.required.JwtDecoder;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.account.service.AccountValidator;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.shared.AuthenticationException;
import java.util.Objects;
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

  private final JwtDecoder jwtProvider;
  private final AccountValidator accountValidator;
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
          AuthenticationAccount annotation =
              Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationAccount.class));
          accountValidator.validateAccountPermission(account, annotation.permission());
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
