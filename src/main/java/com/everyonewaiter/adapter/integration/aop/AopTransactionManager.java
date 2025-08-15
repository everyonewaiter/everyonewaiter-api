package com.everyonewaiter.adapter.integration.aop;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class AopTransactionManager {

  @Transactional(propagation = REQUIRES_NEW)
  public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
    return joinPoint.proceed();
  }

}
