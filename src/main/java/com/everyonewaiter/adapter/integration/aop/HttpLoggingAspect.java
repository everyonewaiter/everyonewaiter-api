package com.everyonewaiter.adapter.integration.aop;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Order(0)
@Aspect
@Component
class HttpLoggingAspect {

  private static final Logger LOGGER = getLogger(HttpLoggingAspect.class);

  private static final String DELIMITER = ", ";

  @Pointcut("execution(* com.everyonewaiter.adapter.web.api..*.*(..))")
  public void controller() {
    // Pointcut for all controllers
  }

  @Pointcut("@annotation(com.everyonewaiter.application.support.ExcludeLogging)")
  public void exclude() {
    // Pointcut for all methods annotated with @ExcludeLogging
  }

  @Around("controller() && !exclude()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    String handler = getHandlerName(joinPoint);
    String parameters = getParameters(joinPoint);

    String requestId = MDC.get("requestId");
    String requestMethod = MDC.get("requestMethod");
    String requestUri = MDC.get("requestUri");
    String requestHeaders = MDC.get("requestHeaders").replace("\n", ",");

    LOGGER.info("[REQUEST] [{} {}] [{}] [{}]", requestMethod, requestUri, requestId, handler);
    LOGGER.info("[REQUEST PARAMETERS] [{}] {}", requestId, parameters);
    LOGGER.info("[REQUEST HEADERS] [{}] {}", requestId, requestHeaders);

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    try {
      Object result = joinPoint.proceed();

      LOGGER.info("[RESPONSE BODY] [{}] {}", requestId, result);

      return result;
    } finally {
      stopWatch.stop();

      LOGGER.info("[RESPONSE {}ms] [{} {}] [{}] [{}]",
          stopWatch.getTotalTimeMillis(), requestMethod, requestUri, requestId, handler);
    }
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
