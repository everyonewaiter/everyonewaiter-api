package com.everyonewaiter.global.logging;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Aspect
@Component
class HttpRequestLoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestLoggingAspect.class);
  private static final List<String> MASKING_KEYWORDS = List.of("password", "token");
  private static final Map<String, Pattern> MASKING_PATTERNS = MASKING_KEYWORDS.stream()
      .collect(Collectors.toUnmodifiableMap(
          keyword -> keyword,
          keyword -> Pattern.compile("(?i)(" + Pattern.quote(keyword) + "=)([^,)]*)")
      ));
  private static final String DELIMITER = ", ";

  @Pointcut("execution(* com.everyonewaiter.presentation..*.*(..))")
  public void controller() {
    // Pointcut for all controllers
  }

  @Before("controller()")
  public void before(JoinPoint joinPoint) {
    String handler = getHandlerName(joinPoint);
    String parameters = masking(getParameters(joinPoint));

    String requestId = MDC.get("requestId");
    String requestMethod = MDC.get("requestMethod");
    String requestURI = MDC.get("requestURI");
    String requestHeaders = MDC.get("requestHeaders").replace("\n", ",");

    LOGGER.info("[REQUEST] [{} {}] [{}] [{}]", requestMethod, requestURI, requestId, handler);
    LOGGER.info("[REQUEST PARAMETERS] [{}] {}", requestId, parameters);
    LOGGER.info("[REQUEST HEADERS] [{}] {}", requestId, requestHeaders);
  }

  private String getHandlerName(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String argumentTypes = Arrays.stream(joinPoint.getArgs())
        .filter(Objects::nonNull)
        .map(arg -> arg.getClass().getSimpleName())
        .collect(Collectors.joining(DELIMITER));
    return signature.getDeclaringTypeName()
        + "#"
        + signature.getName()
        + "("
        + argumentTypes
        + ")";
  }

  private String getParameters(JoinPoint joinPoint) {
    return Arrays.stream(joinPoint.getArgs())
        .filter(Objects::nonNull)
        .map(Objects::toString)
        .collect(Collectors.joining(DELIMITER));
  }

  private String masking(String parameters) {
    for (Entry<String, Pattern> entry : MASKING_PATTERNS.entrySet()) {
      Pattern pattern = entry.getValue();
      Matcher matcher = pattern.matcher(parameters);

      StringBuilder stringBuilder = new StringBuilder();
      while (matcher.find()) {
        matcher.appendReplacement(stringBuilder, matcher.group(1) + "BLIND");
      }
      matcher.appendTail(stringBuilder);

      parameters = stringBuilder.toString();
    }
    return parameters;
  }

}
