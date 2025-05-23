package com.everyonewaiter.global.domain;

import com.everyonewaiter.domain.device.entity.Device;
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
class StoreOpenValidationAspect {

  private final StoreValidator storeValidator;

  @Pointcut("execution(* com.everyonewaiter.presentation..*.*(..))")
  public void controller() {
    // Pointcut for all controllers
  }

  @Pointcut("@annotation(com.everyonewaiter.global.annotation.StoreOpen)")
  public void storeOpen() {
    // Pointcut for methods annotated with @StoreOpen
  }

  @Before("controller() && storeOpen()")
  public void validateStoreOwner(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof Device device) {
        storeValidator.validateOpen(device.getStoreId());
        break;
      }
    }
  }

}
