package com.everyonewaiter.adapter.integration.aop;

import com.everyonewaiter.application.store.provided.StoreValidator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Aspect
@Component
@RequiredArgsConstructor
class StoreExistValidationAspect {

  private final StoreValidator storeValidator;

  @Pointcut("execution(* com.everyonewaiter.adapter.web.api.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RestController)")
  public void controller() {
    // Pointcut for all controllers
  }

  @Pointcut("@annotation(com.everyonewaiter.domain.store.StoreExist)")
  public void storeExist() {
    // Pointcut for methods annotated with @StoreOpen
  }

  @Before("controller() && storeExist() && args(storeId, ..)")
  public void validateStoreExist(Long storeId) {
    storeValidator.checkExists(storeId);
  }

}
