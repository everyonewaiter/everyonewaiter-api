package com.everyonewaiter.adapter.integration.aop;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.store.service.StoreValidator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Aspect
@Component
@RequiredArgsConstructor
class StoreOwnerValidationAspect {

  private final StoreValidator storeValidator;

  @Pointcut("execution(* com.everyonewaiter.adapter.web.api.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RestController)")
  public void controller() {
    // Pointcut for all controllers
  }

  @Pointcut("@annotation(com.everyonewaiter.domain.store.StoreOwner)")
  public void storeOwner() {
    // Pointcut for methods annotated with @StoreOwner
  }

  @Before("controller() && storeOwner() && args(storeId, ..)")
  public void validateStoreOwner(JoinPoint joinPoint, Long storeId) {
    Object[] args = joinPoint.getArgs();

    for (Object arg : args) {
      if (arg instanceof Account account) {
        storeValidator.validateOwner(storeId, account.getId());
        break;
      }
    }
  }

}
