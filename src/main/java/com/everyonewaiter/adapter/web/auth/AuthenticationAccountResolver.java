package com.everyonewaiter.adapter.web.auth;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.shared.AccessDeniedException;
import com.everyonewaiter.domain.shared.AuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
public class AuthenticationAccountResolver implements HandlerMethodArgumentResolver {

  private static final int COOKIE = 0;
  private static final int HEADER = 1;
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final AccountFinder accountFinder;

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
    int tokenLocation = hasTokenFromCookie(webRequest) ? COOKIE : HEADER;
    String accessToken = extractToken(tokenLocation, webRequest);
    JwtPayload payload = jwtProvider.decode(accessToken).orElseThrow(AuthenticationException::new);

    try {
      Account account = accountFinder.find(payload.getLongId())
          .orElseThrow(AuthenticationException::new);

      AuthenticationAccount annotation = requireNonNull(
          parameter.getParameterAnnotation(AuthenticationAccount.class)
      );

      if (!account.isActive() || !account.hasPermission(annotation.permission())) {
        throw new AccessDeniedException();
      }

      return account;
    } catch (NumberFormatException exception) {
      throw new AuthenticationException();
    }
  }

  private boolean hasTokenFromCookie(NativeWebRequest request) {
    HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();

    if (req.getCookies() == null) {
      return false;
    }

    return Arrays.stream(req.getCookies())
        .anyMatch(cookie -> cookie.getName().equalsIgnoreCase(HttpHeaders.AUTHORIZATION));
  }

  private String extractToken(int tokenLocation, NativeWebRequest request) {
    return switch (tokenLocation) {
      case COOKIE -> {
        HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
        yield Arrays.stream(req.getCookies())
            .filter(cookie -> cookie.getName().equalsIgnoreCase(HttpHeaders.AUTHORIZATION))
            .findAny()
            .map(Cookie::getValue)
            .orElseThrow(AuthenticationException::new);
      }
      case HEADER -> {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
          yield header.substring(BEARER_PREFIX.length());
        } else {
          throw new AuthenticationException();
        }
      }
      default -> throw new AuthenticationException();
    };
  }

}
