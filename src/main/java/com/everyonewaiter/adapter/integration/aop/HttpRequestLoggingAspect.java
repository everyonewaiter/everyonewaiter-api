package com.everyonewaiter.adapter.integration.aop;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Aspect
@Component
class HttpRequestLoggingAspect {

  private static final Logger LOGGER = getLogger(HttpRequestLoggingAspect.class);

  private static final String DELIMITER = ", ";

  @Pointcut(
      "execution(* com.everyonewaiter.adapter.web.api.*.*(..))"
          + " && !@annotation(com.everyonewaiter.application.support.ExcludeLogging)"
          + " && @annotation(org.springframework.web.bind.annotation.RestController)"
  )
  public void controller() {
    // Pointcut for all controllers
  }

  @Before("controller()")
  public void before(JoinPoint joinPoint) {
    String handler = getHandlerName(joinPoint);
    String parameters = getParameters(joinPoint);

    String requestId = MDC.get("requestId");
    String requestMethod = MDC.get("requestMethod");
    String requestUri = MDC.get("requestUri");
    String requestHeaders = MDC.get("requestHeaders").replace("\n", ",");

    LOGGER.info("[REQUEST] [{} {}] [{}] [{}]", requestMethod, requestUri, requestId, handler);
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

}
